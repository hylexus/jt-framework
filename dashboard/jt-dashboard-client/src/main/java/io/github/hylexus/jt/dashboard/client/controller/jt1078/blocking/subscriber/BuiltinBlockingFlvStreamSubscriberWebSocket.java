package io.github.hylexus.jt.dashboard.client.controller.jt1078.blocking.subscriber;

import io.github.hylexus.jt.dashboard.common.consts.DashboardJt1078SessionCloseReason;
import io.github.hylexus.jt.dashboard.common.model.dto.jt1078.DashboardVideoStreamSubscriberDto;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberCreator;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt.utils.JtWebUtils;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.util.UriTemplate;
import reactor.core.scheduler.Scheduler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Slf4j
public class BuiltinBlockingFlvStreamSubscriberWebSocket extends AbstractWebSocketHandler {
    // todo auto-configuration
    public static final String PATH_PATTERN = "/api/dashboard-client/jt1078/video-stream/websocket/flv/{sim}/{channel}";
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final UriTemplate uriTemplate;
    private final Jt1078Publisher jt1078Publisher;
    private final Jt1078SubscriberManager jt1078SubscriberManager;
    private final Jt1078SessionManager sessionManager;

    private final Scheduler scheduler;

    public BuiltinBlockingFlvStreamSubscriberWebSocket(Jt1078Publisher publisher, Jt1078SubscriberManager jt1078SubscriberManager, Scheduler scheduler, Jt1078SessionManager sessionManager) {
        this.jt1078Publisher = publisher;
        this.jt1078SubscriberManager = jt1078SubscriberManager;
        this.sessionManager = sessionManager;
        this.scheduler = scheduler;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        final DashboardVideoStreamSubscriberDto params = parseParam(session, this.uriTemplate);
        log.info("New FLV publisher created via WebSocket: {}", params);
        synchronized (this.sessionMap) {
            sessionMap.put(session.getId(), session);
        }
        final String clientIp = JtWebUtils.getClientIp(session.getHandshakeHeaders()::getFirst)
                .or(() -> Optional.ofNullable(session.getRemoteAddress()).map(InetSocketAddress::getHostName))
                .orElse("");
        final Jt1078SubscriberCreator creator = Jt1078SubscriberCreator.builder()
                .sim(params.getSim())
                .channelNumber(params.getChannel())
                .timeout(Duration.ofSeconds(params.getTimeout()))
                .sourceAudioOptions(BuiltinAudioFormatOptions.parseFrom(params.getSourceAudioHints()).orElse(null))
                .metadata(Map.of(
                        "createdBy", this.getClass().getSimpleName(),
                        "clientIp", clientIp,
                        "clientUri", Optional.ofNullable(session.getUri()).map(URI::toString).orElse(""))
                )
                .build();
        this.jt1078Publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, creator)
                .publishOn(this.scheduler)
                .onErrorComplete(Jt1078SessionDestroyException.class)
                .onErrorComplete(TimeoutException.class)
                .doOnError(Jt1078SessionDestroyException.class, e -> {
                    log.error("取消订阅(Session销毁)");
                })
                .doOnError(TimeoutException.class, e -> {
                    log.error("取消订阅(超时, {} 秒)", params.getTimeout());
                })
                .doOnError(Throwable.class, e -> {
                    log.error(e.getMessage(), e);
                })
                .doOnNext(subscription -> {
                    final byte[] data = subscription.payload();
                    log.info("WebSocket outbound: {}", HexStringUtils.bytes2HexString(data));
                    try {
                        session.sendMessage(new BinaryMessage(data));
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                })
                .doFinally(signalType -> {
                    if (params.isAutoCloseJt1078SessionOnClientClosed()
                            || this.jt1078SubscriberManager.list(params.getSim(), params.getChannel()).findAny().isEmpty()) {
                        this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), DashboardJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
                        log.info("Jt1078SessionClosed By WebSocket: {}", params);
                    }
                    synchronized (this.sessionMap) {
                        final WebSocketSession remove = sessionMap.remove(session.getId());
                        if (remove != null) {
                            try {
                                remove.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                })
                .subscribe();
        log.info("session add : {}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        synchronized (this.sessionMap) {
            final WebSocketSession removed = this.sessionMap.remove(session.getId());
            if (removed != null) {
                removed.close();
            }
        }
        log.info("session {} closed with status  {}", session, closeStatus);
    }

    public static DashboardVideoStreamSubscriberDto parseParam(org.springframework.web.socket.WebSocketSession session, UriTemplate uriTemplate) {
        final URI uri = session.getUri();
        final Map<String, String> values = uriTemplate.match(uri.getPath());
        final String sim = values.getOrDefault("sim", "111111111111");
        final short channel = Numbers.parseInteger(values.getOrDefault("channel", "3")).orElseThrow().shortValue();
        final String query = uri.getQuery();
        if (!StringUtils.hasText(query)) {
            return new DashboardVideoStreamSubscriberDto().setSim(sim).setChannel(channel);
        }

        final String[] arrays = query.split("&");
        final Map<String, String> params = new HashMap<>();
        for (final String item : arrays) {
            final String[] split = item.split("=");
            if (split.length == 2) {
                params.put(split[0], split[1]);
            }
        }
        final int timeout = Numbers.parseInteger(params.get("timeout")).orElse(10);
        return new DashboardVideoStreamSubscriberDto()
                .setSim(sim)
                .setChannel(channel)
                .setTimeout(timeout)
                .setAutoCloseJt1078SessionOnClientClosed(parseBoolean(params, "autoCloseJt1078SessionOnClientClosed", false))
                .setStreamType(Numbers.parseInteger(params.get("streamType")).orElse(0))
                .setDataType(Numbers.parseInteger(params.get("dataType")).orElse(0))
                .setSourceAudioHints(params.get("sourceAudioHints"))
                ;
    }

    public static boolean parseBoolean(Map<String, String> params, String key, boolean def) {
        return Optional.ofNullable(params.get(key)).map(it -> {
            try {
                return Boolean.parseBoolean(it);
            } catch (Exception e) {
                return def;
            }
        }).orElse(def);
    }
}
