package io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.scan.Jt808RequestMsgHandlerScanner;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.Jt808RequestHandlerMappingHandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.SimpleJt808RequestHandlerHandlerMapping;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_INTERCEPTORS;

/**
 * @author hylexus
 */
public class HandlerMappingAutoConfiguration {

    @Bean(BEAN_NAME_JT808_INTERCEPTORS)
    public List<Jt808HandlerInterceptor> jt808HandlerInterceptors(ObjectProvider<Jt808HandlerInterceptor> interceptors) {
        return interceptors.stream()
                .sorted(Comparator.comparing(OrderedComponent::getOrder))
                .collect(Collectors.toList());
    }

    @Bean
    public Jt808HandlerMapping simpleJt808RequestHandlerHandlerMapping(
            ObjectProvider<SimpleJt808RequestHandler<?>> jt808ReqMsgHandlers,
            @Qualifier(BEAN_NAME_JT808_INTERCEPTORS) List<Jt808HandlerInterceptor> interceptors) {

        final ComponentMapping<SimpleJt808RequestHandler<?>> componentMapping = new ComponentMapping<>();
        jt808ReqMsgHandlers.stream().forEach(componentMapping::register);
        return new SimpleJt808RequestHandlerHandlerMapping(componentMapping, interceptors);
    }

    @Bean
    public Jt808HandlerMapping jt808RequestHandlerMappingHandlerMapping(
            ApplicationContext applicationContext,
            Jt808MsgTypeParser msgTypeParser,
            @Qualifier(BEAN_NAME_JT808_INTERCEPTORS) List<Jt808HandlerInterceptor> interceptors) {

        final ComponentMapping<HandlerMethod> msgHandlerComponentMapping = new ComponentMapping<>();
        new Jt808RequestMsgHandlerScanner(applicationContext, msgTypeParser)
                .doScan(msgHandlerComponentMapping);
        return new Jt808RequestHandlerMappingHandlerMapping(msgHandlerComponentMapping, interceptors, msgTypeParser);
    }
}