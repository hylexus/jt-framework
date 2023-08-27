package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.reactive;

import io.github.hylexus.jt.web.reactive.JtResponseWrapperResultHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class JtDashboardReactiveCommonConfiguration {
    @Bean
    public JtResponseWrapperResultHandler jtResponseWrapperResultHandler(
            ServerCodecConfigurer serverCodecConfigurer,
            RequestedContentTypeResolver requestedContentTypeResolver,
            ResponseEntityResultHandler responseEntityResultHandler) {

        return new JtResponseWrapperResultHandler(
                serverCodecConfigurer.getWriters(),
                requestedContentTypeResolver,
                responseEntityResultHandler
        );
    }
}
