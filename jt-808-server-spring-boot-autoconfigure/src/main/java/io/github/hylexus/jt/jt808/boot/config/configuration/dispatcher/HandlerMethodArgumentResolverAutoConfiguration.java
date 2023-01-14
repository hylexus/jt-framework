package io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher;

import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
public class HandlerMethodArgumentResolverAutoConfiguration {

    @Bean
    @Primary
    public Jt808HandlerMethodArgumentResolver handlerMethodArgumentResolver(ObjectProvider<Jt808HandlerMethodArgumentResolver> argumentResolvers) {
        final List<Jt808HandlerMethodArgumentResolver> resolverList = argumentResolvers.stream()
                .filter(e -> e.getClass() != CompositeJt808HandlerMethodArgumentResolver.class)
                .collect(Collectors.toList());
        return new CompositeJt808HandlerMethodArgumentResolver(resolverList);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestBodyHandlerMethodArgumentResolver jt808RequestMsgBodyHandlerMethodArgumentResolver(
            Jt808AnnotationBasedDecoder annotationBasedDecoder) {

        return new Jt808RequestBodyHandlerMethodArgumentResolver(annotationBasedDecoder);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestEntityHandlerMethodArgumentResolver jt808RequestEntityHandlerMethodArgumentResolver(
            Jt808AnnotationBasedDecoder annotationBasedDecoder) {

        return new Jt808RequestEntityHandlerMethodArgumentResolver(annotationBasedDecoder);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestHandlerMethodArgumentResolver jt808RequestArgumentResolver() {
        return new Jt808RequestHandlerMethodArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808ResponseHandlerMethodArgumentResolver jt808ResponseHandlerMethodArgumentResolver() {
        return new Jt808ResponseHandlerMethodArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808ServerExchangeHandlerMethodArgumentResolver jt808ServerExchangeHandlerMethodArgumentResolver() {
        return new Jt808ServerExchangeHandlerMethodArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808SessionHandlerMethodArgumentResolver jt808SessionArgumentResolver() {
        return new Jt808SessionHandlerMethodArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestHeaderHandlerMethodArgumentResolver jt808RequestHeaderArgumentResolver() {
        return new Jt808RequestHeaderHandlerMethodArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionHandlerMethodArgumentResolver jt808ExceptionArgumentResolver() {
        return new ExceptionHandlerMethodArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808FlowIdGeneratorHandlerMethodArgumentResolver jt808FlowIdGeneratorHandlerMethodArgumentResolver() {
        return new Jt808FlowIdGeneratorHandlerMethodArgumentResolver();
    }

}
