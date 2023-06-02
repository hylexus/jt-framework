package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.config;

import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.web.WebSocketSubscriberDemo01;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final Jt1078Publisher jt1078Publisher;

    public WebSocketConfig(Jt1078Publisher jt1078Publisher) {
        this.jt1078Publisher = jt1078Publisher;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), WebSocketSubscriberDemo01.PATH_PATTERN)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketSubscriberDemo01(this.jt1078Publisher);
    }

}