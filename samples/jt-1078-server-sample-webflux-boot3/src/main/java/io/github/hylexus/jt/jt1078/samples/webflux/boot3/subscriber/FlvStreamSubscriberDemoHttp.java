package io.github.hylexus.jt.jt1078.samples.webflux.boot3.subscriber;

import io.github.hylexus.jt.jt1078.samples.webflux.boot3.common.MyJt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.config.WebSocketConfig;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt.utils.JtWebUtils;
import io.github.hylexus.jt808.samples.common.converter.DemoModelConverter;
import io.github.hylexus.jt808.samples.common.dto.Command9101Dto;
import io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto;
import io.github.hylexus.jt808.samples.common.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Slf4j
@Controller
@RequestMapping("/jt1078/subscription/http/flv")
public class FlvStreamSubscriberDemoHttp {

    private final Jt1078SessionManager sessionManager;
    private final Jt1078Publisher publisher;
    private final ProxyService proxyService;

    public FlvStreamSubscriberDemoHttp(Jt1078SessionManager sessionManager, Jt1078Publisher publisher, ProxyService proxyService) {
        this.sessionManager = sessionManager;
        this.publisher = publisher;
        this.proxyService = proxyService;
    }

    @RequestMapping("/{sim}/{channel}")
    public ResponseEntity<Flux<byte[]>> handle(
            ServerWebExchange exchange,
            DemoVideoStreamSubscriberDto params) {

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // exchange.getResponse().getHeaders().setContentType(MediaType.valueOf("video/x-flv"));

        final Flux<byte[]> flvStream = subscribeFlvStream(params, exchange);

        final Flux<byte[]> output = send9101CommandIfNecessary(flvStream, params);

        return ResponseEntity.ok().body(output);
    }

    private Flux<byte[]> send9101CommandIfNecessary(Flux<byte[]> flvStream, DemoVideoStreamSubscriberDto params) {
        if (!params.isAutoSend9101Command()) {
            return flvStream;
        }

        // 自动下发 0x9101 消息
        final Command9101Dto dto = DemoModelConverter.command9101Dto(params);

        // 远程调用 808 服务端，下发 0x9101 指令
        return Flux.from(proxyService.proxy0x9101Msg(buildWebClient(params), dto))
                .onErrorComplete(Throwable.class)
                .doOnError(throwable -> {
                    log.error("下发 0x9101 指令出错", throwable);
                })
                .flatMap(resp -> {
                    if (!resp.success()) {
                        log.error("下发 0x9101 指令出错: {}", resp);
                        return Flux.just("下发 0x9101 指令出错".getBytes(StandardCharsets.UTF_8));
                    }
                    return flvStream;
                });
    }

    private Flux<byte[]> subscribeFlvStream(DemoVideoStreamSubscriberDto params, ServerWebExchange exchange) {
        final String clientIp = JtWebUtils.getClientIp(name -> exchange.getRequest().getHeaders().getFirst(name))
                .or(() -> Optional.ofNullable(exchange.getRequest().getRemoteAddress()).map(InetSocketAddress::getHostName))
                .orElse("");

        final int timeout = params.getTimeout();
        return this.publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, params.getSim(), params.getChannel(), Duration.ofSeconds(timeout))
                .publishOn(WebSocketConfig.SCHEDULER)
                .onErrorComplete(Jt1078SessionDestroyException.class)
                .onErrorComplete(TimeoutException.class)
                .doOnError(Jt1078SessionDestroyException.class, e -> {
                    log.error("取消订阅(Session销毁)");
                })
                .doOnError(TimeoutException.class, e -> {
                    log.error("取消订阅(超时, {} 秒)", timeout);
                })
                .doOnError(Throwable.class, e -> {
                    log.error(e.getMessage(), e);
                })
                .doOnNext(subscription -> {
                    final byte[] payload = subscription.payload();
                    log.info("Http outbound {}", FormatUtils.toHexString(payload));
                })
                .map(ByteArrayJt1078Subscription::payload)
                .doFinally(signalType -> {
                    log.info("Http outbound complete with signal: {}", signalType);
                    if (params.isAutoCloseJt1078SessionOnClientClosed()) {
                        this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), MyJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
                        log.info("Jt1078SessionClosed By HttpStream: {}", params);
                    }
                });
    }

    private WebClient buildWebClient(DemoVideoStreamSubscriberDto params) {
        return WebClient.builder()
                .baseUrl("http://" + params.getJt808ServerIp() + ":" + params.getJt808ServerPortHttp())
                .build();
    }
}
