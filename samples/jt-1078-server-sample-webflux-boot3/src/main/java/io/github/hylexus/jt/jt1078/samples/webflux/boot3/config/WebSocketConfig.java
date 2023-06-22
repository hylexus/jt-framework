package io.github.hylexus.jt.jt1078.samples.webflux.boot3.config;

import io.github.hylexus.jt.jt1078.samples.webflux.boot3.web.WebSocketSubscriberDemoH264;
import io.github.hylexus.jt.jt1078.samples.webflux.boot3.web.WebSocketSubscriberDemoFlv;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {
    @Bean
    public HandlerMapping webSocketMapping(Jt1078Publisher h264Jt1078Publisher, Jt1078SessionManager sessionManager) {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();
        handlerMap.put(WebSocketSubscriberDemoH264.PATH_PATTERN, new WebSocketSubscriberDemoH264(h264Jt1078Publisher, sessionManager));
        handlerMap.put(WebSocketSubscriberDemoFlv.PATH_PATTERN, new WebSocketSubscriberDemoFlv(h264Jt1078Publisher, sessionManager));
        simpleUrlHandlerMapping.setUrlMap(handlerMap);
        simpleUrlHandlerMapping.setOrder(-1);
        return simpleUrlHandlerMapping;
    }

    // WebFlux 环境下不需要这个配置
    // @Bean
    // public WebSocketHandlerAdapter handlerAdapter() {
    //     return new WebSocketHandlerAdapter();
    // }
}