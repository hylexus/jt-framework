package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.reactive;

import io.github.hylexus.jt.dashboard.client.actuator.jt1078.reactive.subscriber.FlvStreamSubscriberDemoWebSocket;
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
public class BuiltinWebSocketConfiguration {

    @Bean
    public HandlerMapping webSocketMapping(
            Jt1078Publisher h264Jt1078Publisher,
            Jt1078SessionManager sessionManager) {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();

        // 经过转换之后的 FLV 视频流示例
        handlerMap.put(FlvStreamSubscriberDemoWebSocket.PATH_PATTERN, new FlvStreamSubscriberDemoWebSocket(h264Jt1078Publisher, sessionManager));

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
