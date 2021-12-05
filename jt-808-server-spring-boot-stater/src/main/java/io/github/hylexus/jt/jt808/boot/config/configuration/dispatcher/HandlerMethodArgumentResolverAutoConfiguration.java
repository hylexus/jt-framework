package io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher;

import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.HandlerMethodArgumentResolver;
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
    @ConditionalOnMissingBean
    public Jt808RequestMsgBodyHandlerMethodArgumentResolver jt808RequestMsgBodyHandlerMethodArgumentResolver(
            Jt808AnnotationBasedDecoder annotationBasedDecoder) {

        return new Jt808RequestMsgBodyHandlerMethodArgumentResolver(annotationBasedDecoder);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestArgumentResolver jt808RequestArgumentResolver() {
        return new Jt808RequestArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808SessionArgumentResolver jt808SessionArgumentResolver() {
        return new Jt808SessionArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestHeaderArgumentResolver jt808RequestHeaderArgumentResolver() {
        return new Jt808RequestHeaderArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808ExceptionArgumentResolver jt808ExceptionArgumentResolver() {
        return new Jt808ExceptionArgumentResolver();
    }

    @Bean
    @Primary
    public HandlerMethodArgumentResolver handlerMethodArgumentResolver(ObjectProvider<HandlerMethodArgumentResolver> argumentResolvers) {
        final List<HandlerMethodArgumentResolver> resolverList = argumentResolvers.stream()
                .filter(e -> e.getClass() != CompositeJt808HandlerMethodArgumentResolver.class)
                .collect(Collectors.toList());
        return new CompositeJt808HandlerMethodArgumentResolver(resolverList);
    }

}
