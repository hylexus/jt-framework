package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.config;

import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber.FlvStreamSubscriberDemoWebSocket;
import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber.H264StreamSubscriberDemoWebSocket;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final Jt1078Publisher jt1078Publisher;

    public WebSocketConfig(Jt1078Publisher jt1078Publisher) {
        this.jt1078Publisher = jt1078Publisher;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketSubscriberDemoH264(), H264StreamSubscriberDemoWebSocket.PATH_PATTERN)
                .addHandler(webSocketSubscriberDemoFlv(), FlvStreamSubscriberDemoWebSocket.PATH_PATTERN)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler webSocketSubscriberDemoH264() {
        return new H264StreamSubscriberDemoWebSocket(this.jt1078Publisher);
    }

    @Bean
    public WebSocketHandler webSocketSubscriberDemoFlv() {
        return new FlvStreamSubscriberDemoWebSocket(this.jt1078Publisher);
    }

    public static final Scheduler SCHEDULER = Schedulers.newBoundedElastic(10, 1024, "customSubScriber");
}
