package io.github.hylexus.jt.jt1078.samples.webflux.boot3.config;

import io.github.hylexus.jt.jt1078.samples.webflux.boot3.web.WebSocketSubscriberDemo01;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {
    @Bean
    public HandlerMapping webSocketMapping(Jt1078Publisher h264Jt1078Publisher1) {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();
        handlerMap.put(WebSocketSubscriberDemo01.PATH_PATTERN, new WebSocketSubscriberDemo01(h264Jt1078Publisher1));
        simpleUrlHandlerMapping.setUrlMap(handlerMap);
        simpleUrlHandlerMapping.setOrder(-1);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}