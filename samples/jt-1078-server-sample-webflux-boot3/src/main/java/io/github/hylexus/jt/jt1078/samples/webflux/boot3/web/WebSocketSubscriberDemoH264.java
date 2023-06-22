package io.github.hylexus.jt.jt1078.samples.webflux.boot3.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.common.MyJt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.model.vo.WebSocketSubscriberVo;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
public class WebSocketSubscriberDemoH264 implements WebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/h264/{sim}/{channel}";
    private final Jt1078Publisher jt1078Publisher;
    private final UriTemplate uriTemplate;

    private final Jt1078SessionManager sessionManager;

    public WebSocketSubscriberDemoH264(Jt1078Publisher jt1078Publisher, Jt1078SessionManager sessionManager) {
        this.jt1078Publisher = jt1078Publisher;
        this.sessionManager = sessionManager;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull WebSocketSession session) {

        final WebSocketSubscriberVo params = WebSocketSubscriberVo.of(session, this.uriTemplate);
        log.info("New H.264(RAW) publisher created via WebSocket: {}", params);

        final Mono<Void> input = session.receive()
                .doOnNext(message -> log.info("Receive webSocket msg, webSocketSessionId={}, payload={}", session.getId(), message.getPayloadAsText()))
                .then();

        final ObjectMapper objectMapper = this.createObjectMapper(params.byteArrayAsBase64());

        final Mono<Void> output = this.jt1078Publisher.subscribe(Jt1078ChannelCollector.RAW_DATA_COLLECTOR, params.sim(), params.channel(), Duration.ofSeconds(params.timeout()))
                .filter(it -> it.header().payloadType() == DefaultJt1078PayloadType.H264)
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
            this.sessionManager.removeBySimAndClose(params.sim(), MyJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
            log.info("Jt1078SessionClosed By WebSocket: {}", params);
        }).then();

    }

    static class ByteArrayModule extends com.fasterxml.jackson.databind.module.SimpleModule {
        ByteArrayModule() {
            addSerializer(byte[].class, new ByteArraySerializer());
        }
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

    static class ByteArraySerializer extends StdSerializer<byte[]> {

        ByteArraySerializer() {
            super(byte[].class);
        }

        @Override
        public void serialize(byte[] bytes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray();
            for (byte b : bytes) {
                jsonGenerator.writeNumber(b & 0xff);
            }
            jsonGenerator.writeEndArray();
        }
    }
}