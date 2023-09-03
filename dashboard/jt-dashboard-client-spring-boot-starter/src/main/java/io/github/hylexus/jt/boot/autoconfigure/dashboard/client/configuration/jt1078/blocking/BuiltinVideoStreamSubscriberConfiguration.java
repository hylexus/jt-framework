package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.blocking;

import io.github.hylexus.jt.dashboard.client.controller.jt1078.reactive.subscriber.BuiltinFlvStreamSubscriberHttp;
import io.github.hylexus.jt.dashboard.client.controller.jt1078.reactive.subscriber.BuiltinFlvStreamSubscriberWebSocket;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class BuiltinVideoStreamSubscriberConfiguration {

    @Bean
    public HandlerMapping builtinFlvStreamSubscriberWebSocketMapping(
            Jt1078Publisher h264Jt1078Publisher,
            Jt1078SessionManager sessionManager) {
        final SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();

        handlerMap.put(BuiltinFlvStreamSubscriberWebSocket.PATH_PATTERN, new BuiltinFlvStreamSubscriberWebSocket(h264Jt1078Publisher, sessionManager));

        simpleUrlHandlerMapping.setUrlMap(handlerMap);
        simpleUrlHandlerMapping.setOrder(-1);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public BuiltinFlvStreamSubscriberHttp builtinFlvStreamSubscriberHttp(Jt1078SessionManager sessionManager, Jt1078Publisher publisher) {
        return new BuiltinFlvStreamSubscriberHttp(sessionManager, publisher);
    }
}
