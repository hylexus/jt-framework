package io.github.hylexus.jt808.samples.annotation.configuration;

import io.github.hylexus.jt808.samples.annotation.configuration.web.ResponseWrapperResultHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;

@Configuration
public class WebFluxConfiguration {
    @Bean
    public ResponseWrapperResultHandler responseWrapperResultHandler(
            ServerCodecConfigurer serverCodecConfigurer,
            RequestedContentTypeResolver requestedContentTypeResolver,
            ResponseEntityResultHandler responseEntityResultHandler) {

        return new ResponseWrapperResultHandler(
                serverCodecConfigurer.getWriters(),
                requestedContentTypeResolver,
                responseEntityResultHandler
        );
    }
}
