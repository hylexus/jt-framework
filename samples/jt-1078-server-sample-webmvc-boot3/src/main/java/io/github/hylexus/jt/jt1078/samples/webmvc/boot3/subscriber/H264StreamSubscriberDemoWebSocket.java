package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber;

import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.common.WebSocketUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
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

@Slf4j
public class H264StreamSubscriberDemoWebSocket extends AbstractWebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/h264/{sim}/{channel}";
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final UriTemplate uriTemplate;
    private final Jt1078Publisher publisher;

    public H264StreamSubscriberDemoWebSocket(Jt1078Publisher publisher) {
        this.publisher = publisher;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        final DemoVideoStreamSubscriberDto params = WebSocketUtils.createForBlockingSession(session, this.uriTemplate);
        log.info("New H.264(RAW) publisher created via WebSocket: {}", params);
        synchronized (this.sessionMap) {
            sessionMap.put(session.getId(), session);
        }
        log.info("session add : {}", session);

        this.publisher.subscribe(Jt1078ChannelCollector.RAW_DATA_COLLECTOR, params.getSim(), params.getChannel(), Duration.ofSeconds(params.getTimeout()))
                .doOnError(Jt1078SessionDestroyException.class, e -> {
                    log.error("Session Destroy... subscribe complete");
                })
                .onErrorComplete(Jt1078SessionDestroyException.class)
                .filter(it -> it.header().payloadType() == DefaultJt1078PayloadType.H264)
                .doOnNext(subscription -> {
                    final byte[] data = subscription.payload();
                    log.info("WebSocket outbound: {}", HexStringUtils.bytes2HexString(data));
                    try {
                        session.sendMessage(new BinaryMessage(data));
                    } catch (IOException e) {
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
