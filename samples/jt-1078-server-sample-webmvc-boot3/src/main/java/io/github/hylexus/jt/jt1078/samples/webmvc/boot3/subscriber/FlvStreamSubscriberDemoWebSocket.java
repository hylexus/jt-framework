package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber;

import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.common.WebSocketUtils;
import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.config.WebSocketConfig;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Slf4j
public class FlvStreamSubscriberDemoWebSocket extends AbstractWebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/websocket/flv/{sim}/{channel}";
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final UriTemplate uriTemplate;
    private final Jt1078Publisher publisher;

    public FlvStreamSubscriberDemoWebSocket(Jt1078Publisher publisher) {
        this.publisher = publisher;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        final DemoVideoStreamSubscriberDto params = WebSocketUtils.createForBlockingSession(session, this.uriTemplate);
        log.info("New FLV publisher created via WebSocket: {}", params);
        synchronized (this.sessionMap) {
            sessionMap.put(session.getId(), session);
        }

        this.publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, params.getSim(), params.getChannel(), Duration.ofSeconds(params.getTimeout()))
                .publishOn(WebSocketConfig.SCHEDULER)
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

}
