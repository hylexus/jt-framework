package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.blocking;

import io.github.hylexus.jt.dashboard.client.controller.jt1078.reactive.subscriber.BuiltinFlvStreamSubscriberHttp;
import io.github.hylexus.jt.dashboard.client.controller.jt1078.reactive.subscriber.BuiltinFlvStreamSubscriberWebSocket;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.scheduler.Scheduler;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class BuiltinVideoStreamSubscriberConfiguration {

    @Bean
    public HandlerMapping builtinFlvStreamSubscriberWebSocketMapping(
            Jt1078Publisher h264Jt1078Publisher,
            Jt1078SubscriberManager jt1078SubscriberManager,
            Jt1078SessionManager sessionManager,
            @Qualifier("dashboardJt1078FlvPlayerScheduler") Scheduler scheduler) {

        final SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();

        handlerMap.put(BuiltinFlvStreamSubscriberWebSocket.PATH_PATTERN, new BuiltinFlvStreamSubscriberWebSocket(h264Jt1078Publisher, jt1078SubscriberManager, scheduler, sessionManager));

        simpleUrlHandlerMapping.setUrlMap(handlerMap);
        simpleUrlHandlerMapping.setOrder(-1);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public BuiltinFlvStreamSubscriberHttp builtinFlvStreamSubscriberHttp(Jt1078SessionManager sessionManager, Jt1078SubscriberManager jt1078SubscriberManager, Jt1078Publisher publisher, @Qualifier("dashboardJt1078FlvPlayerScheduler") Scheduler scheduler) {
        return new BuiltinFlvStreamSubscriberHttp(sessionManager, jt1078SubscriberManager, publisher, scheduler);
    }
}
