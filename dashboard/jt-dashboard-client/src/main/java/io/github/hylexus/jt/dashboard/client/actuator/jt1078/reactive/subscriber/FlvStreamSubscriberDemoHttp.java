package io.github.hylexus.jt.dashboard.client.actuator.jt1078.reactive.subscriber;

import io.github.hylexus.jt.dashboard.common.consts.MyJt1078SessionCloseReason;
import io.github.hylexus.jt.dashboard.common.model.dto.jt1078.WebSocketVideoStreamSubscriberDto;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Controller
@RequestMapping("/jt1078/subscription/http/flv")
public class FlvStreamSubscriberDemoHttp {
    // todo auto-configuration
    public static final Scheduler SCHEDULER = Schedulers.newBoundedElastic(10, 1024, "demo-http");
    private final Jt1078SessionManager sessionManager;
    private final Jt1078Publisher publisher;

    public FlvStreamSubscriberDemoHttp(Jt1078SessionManager sessionManager, Jt1078Publisher publisher) {
        this.sessionManager = sessionManager;
        this.publisher = publisher;
    }

    @RequestMapping("/{sim}/{channel}")
    public ResponseEntity<Flux<byte[]>> handle(
            ServerWebExchange exchange,
            WebSocketVideoStreamSubscriberDto params) {

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // exchange.getResponse().getHeaders().setContentType(MediaType.valueOf("video/x-flv"));

        final Flux<byte[]> flvStream = this.subscribeFlvStream(params);

        return ResponseEntity.ok().body(flvStream);
    }

    private Flux<byte[]> subscribeFlvStream(WebSocketVideoStreamSubscriberDto params) {
        final int timeout = params.getTimeout();
        return this.publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, params.getSim(), params.getChannel(), Duration.ofSeconds(timeout))
                .publishOn(SCHEDULER)
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
                        this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), MyJt1078SessionCloseReason.CLOSED_BY_HTTP);
                        log.info("Jt1078SessionClosed By HttpStream: {}", params);
                    }
                });
    }

}
