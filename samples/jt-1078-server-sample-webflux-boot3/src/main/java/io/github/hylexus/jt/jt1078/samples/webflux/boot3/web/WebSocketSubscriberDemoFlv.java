package io.github.hylexus.jt.jt1078.samples.webflux.boot3.web;

import io.github.hylexus.jt.jt1078.samples.webflux.boot3.common.MyJt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.model.vo.WebSocketSubscriberVo;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
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

import java.time.Duration;

@Slf4j
@Component
public class WebSocketSubscriberDemoFlv implements WebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/flv/{sim}/{channel}";
    private final Jt1078Publisher jt1078Publisher;
    private final UriTemplate uriTemplate;

    private final Jt1078SessionManager sessionManager;

    public WebSocketSubscriberDemoFlv(Jt1078Publisher jt1078Publisher, Jt1078SessionManager sessionManager) {
        this.jt1078Publisher = jt1078Publisher;
        this.sessionManager = sessionManager;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull WebSocketSession session) {

        final WebSocketSubscriberVo params = WebSocketSubscriberVo.of(session, this.uriTemplate);
        log.info("New FLV publisher created via WebSocket: {}", params);

        final Mono<Void> input = session.receive()
                .doOnNext(message -> {
                    log.info("Receive webSocket msg, webSocketSessionId={}, payload={}", session.getId(), message.getPayloadAsText());
                })
                .then();

        final Mono<Void> output = this.jt1078Publisher
                .subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, params.sim(), params.channel(), Duration.ofSeconds(params.timeout()))
                .flatMap(subscription -> {
                    final byte[] data = subscription.payload();

                    log.info("FLV WebSocket outbound: {}", HexStringUtils.bytes2HexString(data));

                    final WebSocketMessage webSocketMessage = session.binaryMessage(factory -> factory.wrap(data));
                    return Mono.just(webSocketMessage);
                })
                .flatMap(it -> session.send(Mono.just(it)))
                .then();

        return Mono.zip(input, output).doFinally(signalType -> {
            this.sessionManager.removeBySimAndClose(params.sim(), MyJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
            log.info("Jt1078SessionClosed By WebSocket: {}", params);
        }).then();

    }

}