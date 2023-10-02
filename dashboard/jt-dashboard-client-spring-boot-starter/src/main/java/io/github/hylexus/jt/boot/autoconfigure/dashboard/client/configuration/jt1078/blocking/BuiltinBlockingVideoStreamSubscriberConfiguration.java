package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.blocking;

import io.github.hylexus.jt.dashboard.client.controller.jt1078.blocking.subscriber.BuiltinBlockingFlvStreamSubscriberHttp;
import io.github.hylexus.jt.dashboard.client.controller.jt1078.blocking.subscriber.BuiltinBlockingFlvStreamSubscriberWebSocket;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import reactor.core.scheduler.Scheduler;

@Configuration
@EnableWebSocket
public class BuiltinBlockingVideoStreamSubscriberConfiguration implements WebSocketConfigurer {

    private final Jt1078Publisher jt1078Publisher;
    private final Jt1078SubscriberManager jt1078SubscriberManager;
    private final Jt1078SessionManager sessionManager;
    private final Scheduler scheduler;

    public BuiltinBlockingVideoStreamSubscriberConfiguration(
            Jt1078Publisher jt1078Publisher,
            Jt1078SubscriberManager jt1078SubscriberManager,
            Jt1078SessionManager sessionManager,
            @Qualifier("dashboardJt1078FlvPlayerScheduler") Scheduler scheduler) {
        this.jt1078Publisher = jt1078Publisher;
        this.jt1078SubscriberManager = jt1078SubscriberManager;
        this.sessionManager = sessionManager;
        this.scheduler = scheduler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new BuiltinBlockingFlvStreamSubscriberWebSocket(jt1078Publisher, jt1078SubscriberManager, scheduler, sessionManager), BuiltinBlockingFlvStreamSubscriberWebSocket.PATH_PATTERN)
                .setAllowedOrigins("*");
    }

    @Bean
    public BuiltinBlockingFlvStreamSubscriberHttp builtinFlvStreamSubscriberHttp(Jt1078SessionManager sessionManager, Jt1078SubscriberManager jt1078SubscriberManager, Jt1078Publisher publisher, @Qualifier("dashboardJt1078FlvPlayerScheduler") Scheduler scheduler) {
        return new BuiltinBlockingFlvStreamSubscriberHttp(sessionManager, jt1078SubscriberManager, publisher, scheduler);
    }

}
