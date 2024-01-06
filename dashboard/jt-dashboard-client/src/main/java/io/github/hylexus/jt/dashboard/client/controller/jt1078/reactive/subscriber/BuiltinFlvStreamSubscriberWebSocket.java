package io.github.hylexus.jt.dashboard.client.controller.jt1078.reactive.subscriber;

import io.github.hylexus.jt.dashboard.client.controller.jt1078.model.converter.DashboardClientModelConverter;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * H.264 --> FLV
 */
@Slf4j
public class BuiltinFlvStreamSubscriberWebSocket implements WebSocketHandler {
    // todo auto-configuration
    public static final String PATH_PATTERN = "/api/dashboard-client/jt1078/video-stream/websocket/flv/{sim}/{channel}";
    private final Jt1078Publisher jt1078Publisher;
    private final Jt1078SubscriberManager jt1078SubscriberManager;
    private final UriTemplate uriTemplate;
    private final Scheduler scheduler;
    private final Jt1078SessionManager sessionManager;

    public BuiltinFlvStreamSubscriberWebSocket(Jt1078Publisher jt1078Publisher, Jt1078SubscriberManager jt1078SubscriberManager, Scheduler scheduler, Jt1078SessionManager sessionManager) {
        this.jt1078Publisher = jt1078Publisher;
        this.jt1078SubscriberManager = jt1078SubscriberManager;
        this.scheduler = scheduler;
        this.sessionManager = sessionManager;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull WebSocketSession session) {

        final DashboardVideoStreamSubscriberDto params = DashboardClientModelConverter.convert(session, this.uriTemplate);
        log.info("New FLV publisher created via WebSocket: {}", params);

        // websocket inbound
        final Mono<Void> input = session.receive()
                .doOnNext(message -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Receive webSocket msg, webSocketSessionId={}, payload={}", session.getId(), message.getPayloadAsText());
                    }
                })
                .then();

        // FLV 数据流
        final Mono<Void> flvStream = this.subscribeFlv(session, params);

        // websocket outbound
        return Mono.zip(input, flvStream).doFinally(signalType -> {
            if (params.isAutoCloseJt1078SessionOnClientClosed()
                    || this.jt1078SubscriberManager.list(params.getSim(), params.getChannel()).findAny().isEmpty()) {
                this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), DashboardJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
                log.info("Jt1078SessionClosed By WebSocket: {}", params);
            }
        }).then();
    }

    private Mono<Void> subscribeFlv(WebSocketSession session, DashboardVideoStreamSubscriberDto params) {

        final String clientIp = JtWebUtils.getClientIp(session.getHandshakeInfo().getHeaders()::getFirst)
                .or(() -> Optional.ofNullable(session.getHandshakeInfo().getRemoteAddress()).map(InetSocketAddress::getHostName))
                .orElse("");

        final Jt1078SubscriberCreator creator = Jt1078SubscriberCreator.builder()
                .sim(params.getSim())
                .channelNumber(params.getChannel())
                .timeout(Duration.ofSeconds(params.getTimeout()))
                .sourceAudioOptions(BuiltinAudioFormatOptions.parseFrom(params.getSourceAudioHints()).orElse(null))
                .metadata(Map.of(
                        "createdBy", this.getClass().getSimpleName(),
                        "clientIp", clientIp,
                        "clientUri", session.getHandshakeInfo().getUri().toString())
                )
                .build();
        return this.jt1078Publisher
                .subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, creator)
                .publishOn(this.scheduler)
                .onErrorComplete(Jt1078SessionDestroyException.class)
                .onErrorComplete(TimeoutException.class)
                .doOnError(Jt1078SessionDestroyException.class, e -> {
                    log.info("取消订阅(Session销毁)");
                })
                .doOnError(TimeoutException.class, e -> {
                    log.info("取消订阅(超时, {} 秒)", params.getTimeout());
                })
                .doOnError(Throwable.class, e -> {
                    log.error(e.getMessage(), e);
                })
                .flatMap(subscription -> {
                    final byte[] data = subscription.payload();

                    if (log.isDebugEnabled()) {
                        log.debug("FLV WebSocket outbound: {}", HexStringUtils.bytes2HexString(data));
                    }

                    final WebSocketMessage webSocketMessage = session.binaryMessage(factory -> factory.wrap(data));
                    return Mono.just(webSocketMessage);
                })
                .flatMap(it -> session.send(Mono.just(it)))
                .then();
    }

}
