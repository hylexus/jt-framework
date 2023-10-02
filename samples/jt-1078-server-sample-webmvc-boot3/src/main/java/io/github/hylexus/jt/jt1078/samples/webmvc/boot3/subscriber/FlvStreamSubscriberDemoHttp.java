package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber;

import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.common.MyJt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.config.WebSocketConfig;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt808.samples.common.converter.DemoModelConverter;
import io.github.hylexus.jt808.samples.common.dto.Command9101Dto;
import io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto;
import io.github.hylexus.jt808.samples.common.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.Duration;
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

    @RequestMapping(value = "/{sim}/{channel}")
    public ResponseBodyEmitter handle(
            // HttpServletResponse response,
            DemoVideoStreamSubscriberDto params) {

        // response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        final ResponseBodyEmitter sseEmitter = new ResponseBodyEmitter(0L);
        this.subscribeFlvStream(params, sseEmitter);
        return sseEmitter;
    }

    private void subscribeFlvStream(DemoVideoStreamSubscriberDto params, ResponseBodyEmitter sseEmitter) {
        final int timeout = params.getTimeout();
        this.publisher.subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, params.getSim(), params.getChannel(), Duration.ofSeconds(timeout))
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
                    // 异常结束
                    sseEmitter.completeWithError(e);
                })
                .doOnNext(subscription -> {
                    final byte[] payload = subscription.payload();
                    log.info("Http outbound {}", FormatUtils.toHexString(payload));
                })
                .doFinally(signalType -> {
                    log.info("Http outbound complete with signal: {}", signalType);
                    if (params.isAutoCloseJt1078SessionOnClientClosed()) {
                        this.sessionManager.removeBySimAndChannelAndThenClose(params.getSim(), params.getChannel(), MyJt1078SessionCloseReason.CLOSED_BY_WEB_SOCKET);
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

    private void send9101CommandIfNecessary(DemoVideoStreamSubscriberDto params) {
        if (!params.isAutoSend9101Command()) {
            return;
        }

        // 自动下发 0x9101 消息
        final Command9101Dto dto = DemoModelConverter.command9101Dto(params);

        // 远程调用 808 服务端，下发 0x9101 指令
        final Resp<Object> resp = proxyService.proxy0x9101Msg(buildWebClient(params), dto).block();
        if (Resp.isFailure(resp)) {
            throw new RuntimeException("下发 0x9101 指令出错");
        }
    }

    private WebClient buildWebClient(DemoVideoStreamSubscriberDto params) {
        return WebClient.builder()
                .baseUrl("http://" + params.getJt808ServerIp() + ":" + params.getJt808ServerPortHttp())
                .build();
    }
}
