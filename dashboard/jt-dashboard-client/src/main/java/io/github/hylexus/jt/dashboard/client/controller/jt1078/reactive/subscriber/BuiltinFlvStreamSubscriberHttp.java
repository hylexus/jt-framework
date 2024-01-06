package io.github.hylexus.jt.dashboard.client.controller.jt1078.reactive.subscriber;

import io.github.hylexus.jt.dashboard.common.consts.DashboardJt1078SessionCloseReason;
import io.github.hylexus.jt.dashboard.common.model.dto.jt1078.DashboardVideoStreamSubscriberDto;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberCreator;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt.utils.JtWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Slf4j
@Controller
@RequestMapping("/api/dashboard-client/jt1078/video-stream/http/flv")
public class BuiltinFlvStreamSubscriberHttp {
    // todo auto-configuration
    private final Jt1078SessionManager sessionManager;
    private final Jt1078SubscriberManager jt1078SubscriberManager;
    private final Jt1078Publisher publisher;
    private final Scheduler scheduler;

    public BuiltinFlvStreamSubscriberHttp(Jt1078SessionManager sessionManager, Jt1078SubscriberManager jt1078SubscriberManager, Jt1078Publisher publisher, Scheduler scheduler) {
        this.sessionManager = sessionManager;
        this.jt1078SubscriberManager = jt1078SubscriberManager;
        this.publisher = publisher;
        this.scheduler = scheduler;
    }

    @RequestMapping("/{sim}/{channel}")
    public ResponseEntity<Flux<byte[]>> handle(
            ServerWebExchange exchange,
            DashboardVideoStreamSubscriberDto params) {

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // exchange.getResponse().getHeaders().setContentType(MediaType.valueOf("video/x-flv"));

        final Flux<byte[]> flvStream = this.subscribeFlvStream(params, exchange);

        return ResponseEntity.ok().body(flvStream);
    }

    private Flux<byte[]> subscribeFlvStream(DashboardVideoStreamSubscriberDto params, ServerWebExchange exchange) {
        final String clientIp = JtWebUtils.getClientIp(exchange.getRequest().getHeaders()::getFirst)
                .or(() -> Optional.ofNullable(exchange.getRequest().getRemoteAddress()).map(InetSocketAddress::getHostName))
                .orElse("");
        final int timeout = params.getTimeout();
        final Jt1078SubscriberCreator creator = Jt1078SubscriberCreator.builder()
                .sim(params.getSim())
                .channelNumber(params.getChannel())
                .timeout(Duration.ofSeconds(params.getTimeout()))
                .sourceAudioOptions(BuiltinAudioFormatOptions.parseFrom(params.getSourceAudioHints()).orElse(null))
                .metadata(Map.of(
                        "createdBy", this.getClass().getSimpleName(),
                        "clientIp", clientIp,
                        "clientUri", exchange.getRequest().getURI().toString())
                )
                .build();
        return this.publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, creator)
                .publishOn(this.scheduler)
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
                    if (log.isDebugEnabled()) {
                        log.debug("Http outbound {}", FormatUtils.toHexString(payload));
                    }
                })
                .map(ByteArrayJt1078Subscription::payload)
                .doFinally(signalType -> {
                    log.info("Http outbound complete with signal: {}", signalType);
                    if (params.isAutoCloseJt1078SessionOnClientClosed()
                            || this.jt1078SubscriberManager.list(params.getSim(), params.getChannel()).findAny().isEmpty()) {
                        this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), DashboardJt1078SessionCloseReason.CLOSED_BY_HTTP);
                        log.info("Jt1078SessionClosed By HttpStream: {}", params);
                    }
                });
    }

}
