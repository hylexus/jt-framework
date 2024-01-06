package io.github.hylexus.jt.dashboard.client.controller.jt1078.blocking.subscriber;

import io.github.hylexus.jt.dashboard.common.consts.DashboardJt1078SessionCloseReason;
import io.github.hylexus.jt.dashboard.common.model.dto.jt1078.DashboardVideoStreamSubscriberDto;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberCreator;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt.utils.JtWebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.scheduler.Scheduler;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Slf4j
@Controller
@RequestMapping("/api/dashboard-client/jt1078/video-stream/http/flv")
public class BuiltinBlockingFlvStreamSubscriberHttp {

    private final Jt1078SessionManager sessionManager;
    private final Jt1078SubscriberManager jt1078SubscriberManager;
    private final Jt1078Publisher publisher;
    private final Scheduler scheduler;

    public BuiltinBlockingFlvStreamSubscriberHttp(Jt1078SessionManager sessionManager, Jt1078SubscriberManager jt1078SubscriberManager, Jt1078Publisher publisher, Scheduler scheduler) {
        this.sessionManager = sessionManager;
        this.jt1078SubscriberManager = jt1078SubscriberManager;
        this.publisher = publisher;
        this.scheduler = scheduler;
    }

    @RequestMapping(value = "/{sim}/{channel}")
    public ResponseBodyEmitter handle(
            HttpServletRequest request,
            // HttpServletResponse response,
            DashboardVideoStreamSubscriberDto params) {

        // response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        final ResponseBodyEmitter sseEmitter = new ResponseBodyEmitter(0L);
        this.subscribeFlvStream(params, sseEmitter, request);
        return sseEmitter;
    }

    private void subscribeFlvStream(DashboardVideoStreamSubscriberDto params, ResponseBodyEmitter sseEmitter, HttpServletRequest request) {
        final int timeout = params.getTimeout();

        final String clientIp = JtWebUtils.getClientIp(request::getHeader)
                .or(() -> Optional.ofNullable(request.getRemoteHost()))
                .orElse("");

        final Jt1078SubscriberCreator creator = Jt1078SubscriberCreator.builder()
                .sim(params.getSim())
                .channelNumber(params.getChannel())
                .timeout(Duration.ofSeconds(params.getTimeout()))
                .sourceAudioOptions(BuiltinAudioFormatOptions.parseFrom(params.getSourceAudioHints()).orElse(null))
                .metadata(Map.of(
                        "createdBy", this.getClass().getSimpleName(),
                        "clientIp", clientIp,
                        "clientUri", request.getRequestURL().toString())
                )
                .build();
        this.publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, creator)
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
                    // 异常结束
                    sseEmitter.completeWithError(e);
                })
                .doOnNext(subscription -> {
                    final byte[] payload = subscription.payload();
                    log.info("Http outbound {}", FormatUtils.toHexString(payload));
                })
                .doFinally(signalType -> {
                    log.info("Http outbound complete with signal: {}", signalType);
                    if (params.isAutoCloseJt1078SessionOnClientClosed()
                            || this.jt1078SubscriberManager.list(params.getSim(), params.getChannel()).findAny().isEmpty()) {
                        this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), DashboardJt1078SessionCloseReason.CLOSED_BY_HTTP);
                        log.info("Jt1078SessionClosed By HttpStream: {}", params);
                    }
                    // 正常结束
                    sseEmitter.complete();
                }).subscribe(subscription -> {
                    final byte[] payload = subscription.payload();
                    try {
                        // 异常结束
                        log.info("send: {}", FormatUtils.toHexString(payload));
                        sseEmitter.send(payload, MediaType.APPLICATION_OCTET_STREAM);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                });
    }
}
