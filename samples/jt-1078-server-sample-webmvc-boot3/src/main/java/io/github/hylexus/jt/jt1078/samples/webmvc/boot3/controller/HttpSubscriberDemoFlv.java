package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.controller;

import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.config.WebSocketConfig;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Controller
@RequestMapping("/jt1078/subscription/http/flv")
public class HttpSubscriberDemoFlv {
    private final Jt1078Publisher publisher;

    public HttpSubscriberDemoFlv(Jt1078Publisher publisher) {
        this.publisher = publisher;
    }

    @RequestMapping(value = "/{sim}/{channel}")
    public ResponseEntity<ResponseBodyEmitter> handle(
            @PathVariable String sim, @PathVariable short channel,
            @RequestParam(value = "timeout", required = false, defaultValue = "1000") int timeout) {

        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        // response.setContentType("application/octet-stream");

        this.publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, sim, channel, Duration.ofSeconds(timeout))
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
                    emitter.completeWithError(e);
                })
                .doOnNext(subscription -> {
                    final byte[] payload = subscription.payload();
                    log.info("Http outbound {}", FormatUtils.toHexString(payload));
                    try {
                        emitter.send(payload, MediaType.APPLICATION_OCTET_STREAM);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                })
                // .delayElements(Duration.ofMillis(100))
                .doFinally(signalType -> {
                    log.info("Http outbound complete with signal: {}", signalType);
                    emitter.complete();
                })
                .subscribe();

        return ResponseEntity.ok().body(emitter);
    }
}
