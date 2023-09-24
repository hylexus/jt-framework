package io.github.hylexus.jt.jt1078.samples.webflux.boot3.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.common.MyJt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.common.WebSocketUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.samples.common.codc.json.ByteArrayModule;
import io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * H.264 裸流
 */
@Slf4j
@Component
public class H264StreamSubscriberDemoWebSocket implements WebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/websocket/h264/{sim}/{channel}";
    private final Jt1078Publisher jt1078Publisher;
    private final UriTemplate uriTemplate;

    private final Jt1078SessionManager sessionManager;

    public H264StreamSubscriberDemoWebSocket(Jt1078Publisher jt1078Publisher, Jt1078SessionManager sessionManager) {
        this.jt1078Publisher = jt1078Publisher;
        this.sessionManager = sessionManager;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull WebSocketSession session) {

        final DemoVideoStreamSubscriberDto params = WebSocketUtils.createForReactiveSession(session, this.uriTemplate);
        log.info("New H.264(RAW) publisher created via WebSocket: {}", params);

        final Mono<Void> input = session.receive()
                .doOnNext(message -> log.info("Receive webSocket msg, webSocketSessionId={}, payload={}", session.getId(), message.getPayloadAsText()))
                .then();

        final ObjectMapper objectMapper = this.createObjectMapper(params.isByteArrayAsBase64());

        final Mono<Void> output = this.jt1078Publisher.subscribe(Jt1078ChannelCollector.RAW_DATA_COLLECTOR, params.getSim(), params.getChannel(), Duration.ofSeconds(params.getTimeout()))
                // 只保留H.264格式的数据
                .filter(it -> it.header().payloadType() == DefaultJt1078PayloadType.H264)
                .doOnError(Jt1078SessionDestroyException.class, e -> {
                    log.error("Session Destroy... subscribe complete");
                })
                .onErrorComplete(Jt1078SessionDestroyException.class)
                .flatMap(subscription -> {
                    final Object resultVo = new WebSocketOutput(subscription.header().payloadType().value(), subscription.payload());
                    final byte[] bytes;
                    try {
                        bytes = objectMapper.writeValueAsBytes(resultVo);
                    } catch (JsonProcessingException e) {
                        return reactor.core.publisher.Flux.error(new RuntimeException(e));
                    }
                    log.info("H264 WebSocket outbound: {}", HexStringUtils.bytes2HexString(subscription.payload()));
                    final WebSocketMessage webSocketMessage = session.binaryMessage(factory -> factory.wrap(bytes));
                    return Mono.just(webSocketMessage);
                })
                .doOnError(throwable -> {
                    log.error("", throwable);
                })
                .flatMap(it -> session.send(Mono.just(it)))
                .then();

        return Mono.zip(input, output).doFinally(signalType -> {
            if (params.isAutoCloseJt1078SessionOnClientClosed()) {
                this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), MyJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
                log.info("Jt1078SessionClosed By WebSocket: {}", params);
            }
        }).then();

    }


    ObjectMapper createObjectMapper(boolean byteArrayAsBase64) {
        final ObjectMapper objectMapper = new ObjectMapper();
        if (!byteArrayAsBase64) {
            objectMapper.registerModule(new ByteArrayModule());
        }
        return objectMapper;
    }


    public record WebSocketOutput(short type, byte[] contentByte) {
    }

}
