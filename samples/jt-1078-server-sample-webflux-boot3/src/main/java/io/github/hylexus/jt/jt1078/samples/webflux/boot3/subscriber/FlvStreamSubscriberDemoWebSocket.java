package io.github.hylexus.jt.jt1078.samples.webflux.boot3.subscriber;

import io.github.hylexus.jt.jt1078.samples.webflux.boot3.common.MyJt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.config.WebSocketConfig;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.samples.common.converter.DemoModelConverter;
import io.github.hylexus.jt808.samples.common.dto.Command9101Dto;
import io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto;
import io.github.hylexus.jt808.samples.common.service.ProxyService;
import io.github.hylexus.jt808.samples.common.vo.DefaultRespCode;
import io.github.hylexus.jt808.samples.common.vo.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

/**
 * H.264 --> FLV
 */
@Slf4j
public class FlvStreamSubscriberDemoWebSocket implements WebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/websocket/flv/{sim}/{channel}";
    private final Jt1078Publisher jt1078Publisher;
    private final UriTemplate uriTemplate;

    private final Jt1078SessionManager sessionManager;
    private final ProxyService proxyService;

    public FlvStreamSubscriberDemoWebSocket(Jt1078Publisher jt1078Publisher, Jt1078SessionManager sessionManager, ProxyService proxyService) {
        this.jt1078Publisher = jt1078Publisher;
        this.sessionManager = sessionManager;
        this.proxyService = proxyService;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull WebSocketSession session) {

        final DemoVideoStreamSubscriberDto params = DemoVideoStreamSubscriberDto.of(session, this.uriTemplate);
        log.info("New FLV publisher created via WebSocket: {}", params);

        // websocket inbound
        final Mono<Void> input = session.receive()
                .doOnNext(message -> {
                    log.info("Receive webSocket msg, webSocketSessionId={}, payload={}", session.getId(), message.getPayloadAsText());
                })
                .then();

        // FLV 数据流
        final Mono<Void> flvStream = this.subscribeFlv(session, params);

        // websocket outbound
        final Mono<Void> output = this.send9101CommandIfNecessary(session, params, flvStream);

        return Mono.zip(input, output).doFinally(signalType -> {
            if (params.isAutoCloseJt1078SessionOnClientClosed()) {
                this.sessionManager.removeBySimAndClose(params.getSim(), MyJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
                log.info("Jt1078SessionClosed By WebSocket: {}", params);
            }
        }).then();
    }

    private Mono<Void> send9101CommandIfNecessary(WebSocketSession session, DemoVideoStreamSubscriberDto params, Mono<Void> flvStream) {
        // 不自动下发 0x9101 消息
        if (!params.isAutoSend9101Command()) {
            return flvStream;
        }

        // 自动下发 0x9101 消息
        final Command9101Dto dto = DemoModelConverter.command9101Dto(params);

        // 远程调用 808 服务端，下发 0x9101 指令
        return this.proxyService.proxy0x9101Msg(buildWebClient(params), dto).onErrorResume(Throwable.class, throwable -> {
            log.error("下发 0x9101 指令出错", throwable);
            return Mono.just(Resp.failure(DefaultRespCode.REMOTE_CALL_ERROR, "下发 0x9101 指令出错; " + throwable.getMessage()));
        }).flatMap(resp -> {
            if (!resp.success()) {
                log.error("下发 0x9101 指令出错: {}", resp);
                return session.send(Mono.just(session.textMessage("下发 0x9101 指令出错: " + resp.getMsg()))).then();
            }
            return flvStream;
        });
    }

    private Mono<Void> subscribeFlv(WebSocketSession session, DemoVideoStreamSubscriberDto params) {
        return this.jt1078Publisher
                .subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, params.getSim(), params.getChannel(), Duration.ofSeconds(params.getTimeout()))
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
                .flatMap(subscription -> {
                    final byte[] data = subscription.payload();

                    log.info("FLV WebSocket outbound: {}", HexStringUtils.bytes2HexString(data));

                    final WebSocketMessage webSocketMessage = session.binaryMessage(factory -> factory.wrap(data));
                    return Mono.just(webSocketMessage);
                })
                .flatMap(it -> session.send(Mono.just(it)))
                .then();
    }

    private WebClient buildWebClient(DemoVideoStreamSubscriberDto params) {
        return WebClient.builder()
                .baseUrl("http://" + params.getJt808ServerIp() + ":" + params.getJt808ServerPortHttp())
                .build();
    }

}