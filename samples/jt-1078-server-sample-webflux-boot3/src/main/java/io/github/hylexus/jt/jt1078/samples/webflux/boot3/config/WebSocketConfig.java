package io.github.hylexus.jt.jt1078.samples.webflux.boot3.config;

import io.github.hylexus.jt.jt1078.samples.webflux.boot3.subscriber.FlvStreamSubscriberDemoWebSocket;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.subscriber.H264StreamSubscriberDemoWebSocket;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt808.samples.common.service.ProxyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {
    @Bean
    public HandlerMapping webSocketMapping(
            Jt1078Publisher h264Jt1078Publisher,
            Jt1078SessionManager sessionManager,
            ProxyService proxyService) {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();

        // H.264 裸流示例
        handlerMap.put(H264StreamSubscriberDemoWebSocket.PATH_PATTERN, new H264StreamSubscriberDemoWebSocket(h264Jt1078Publisher, sessionManager));
        // 经过转换之后的 FLV 视频流示例
        handlerMap.put(FlvStreamSubscriberDemoWebSocket.PATH_PATTERN, new FlvStreamSubscriberDemoWebSocket(h264Jt1078Publisher, sessionManager, proxyService));
        simpleUrlHandlerMapping.setUrlMap(handlerMap);
        simpleUrlHandlerMapping.setOrder(-1);
        return simpleUrlHandlerMapping;
    }

    // WebFlux 环境下不需要这个配置
    // @Bean
    // public WebSocketHandlerAdapter handlerAdapter() {
    //     return new WebSocketHandlerAdapter();
    // }

    public static final Scheduler SCHEDULER = Schedulers.newBoundedElastic(10, 1024, "customSubScriber");
}
