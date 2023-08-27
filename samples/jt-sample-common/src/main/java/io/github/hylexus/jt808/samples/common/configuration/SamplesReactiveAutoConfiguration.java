package io.github.hylexus.jt808.samples.common.configuration;

import io.github.hylexus.jt.web.reactive.JtResponseWrapperResultHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@ConditionalOnWebApplication(type = REACTIVE)
public class SamplesReactiveAutoConfiguration {

    @Bean
    public JtResponseWrapperResultHandler responseWrapperResultHandler(
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
