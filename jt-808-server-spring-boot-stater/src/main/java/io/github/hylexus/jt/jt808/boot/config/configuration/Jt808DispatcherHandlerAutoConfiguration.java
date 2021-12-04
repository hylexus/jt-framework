package io.github.hylexus.jt.jt808.boot.config.configuration;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.converter.MsgTypeParser;
import io.github.hylexus.jt.jt808.support.dispatcher.*;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808ReqMsgHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter.HandlerMethodHandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter.Jt808ReqMsgHandlerHandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler.BuiltinLoggingOnlyExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler.DelegateExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseHandlerResultHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseMsgBodyHandlerResultHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.scan.Jt808RequestMsgHandlerScanner;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.Jt808ReqMsgHandlerHandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.Jt808RequestMsgHandlerAnnotationHandlerMapping;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.hylexus.jt.config.JtProtocolConstant.BEAN_NAME_JT808_INTERCEPTORS;

/**
 * @author hylexus
 */
public class Jt808DispatcherHandlerAutoConfiguration {

    @Bean
    @Primary
    public Jt808ExceptionHandler delegateExceptionHandler() {
        return new DelegateExceptionHandler().addExceptionHandler(new BuiltinLoggingOnlyExceptionHandler());
    }

    @Bean(BEAN_NAME_JT808_INTERCEPTORS)
    public List<Jt808HandlerInterceptor> jt808HandlerInterceptors(ObjectProvider<Jt808HandlerInterceptor> interceptors) {
        return interceptors.stream()
                .sorted(Comparator.comparing(OrderedComponent::getOrder))
                .collect(Collectors.toList());
    }

    @Bean
    public Jt808HandlerMapping jt808ReqMsgHandlerHandlerMapping(
            ObjectProvider<Jt808ReqMsgHandler<?>> jt808ReqMsgHandlers,
            @Qualifier(BEAN_NAME_JT808_INTERCEPTORS) List<Jt808HandlerInterceptor> interceptors) {

        final ComponentMapping<Jt808ReqMsgHandler<?>> componentMapping = new ComponentMapping<>();
        jt808ReqMsgHandlers.stream().forEach(componentMapping::register);
        return new Jt808ReqMsgHandlerHandlerMapping(componentMapping, interceptors);
    }

    @Bean
    public Jt808HandlerMapping jt808HandlerMapping(
            ApplicationContext applicationContext, MsgTypeParser msgTypeParser,
            @Qualifier(BEAN_NAME_JT808_INTERCEPTORS) List<Jt808HandlerInterceptor> interceptors) {

        final ComponentMapping<HandlerMethod> msgHandlerComponentMapping = new ComponentMapping<>();
        new Jt808RequestMsgHandlerScanner(applicationContext, msgTypeParser)
                .doScan(msgHandlerComponentMapping);
        return new Jt808RequestMsgHandlerAnnotationHandlerMapping(msgHandlerComponentMapping, interceptors);
    }

    @Bean
    public Jt808HandlerAdapter jt808ReqMsgHandlerHandlerAdapter() {
        return new Jt808ReqMsgHandlerHandlerAdapter();
    }

    @Bean
    public Jt808HandlerAdapter handlerMethodHandlerAdapter() {
        return new HandlerMethodHandlerAdapter();
    }

    @Bean
    public Jt808HandlerResultHandler jt808ResponseHandlerResultHandler(Jt808MsgEncoder encoder) {
        return new Jt808ResponseHandlerResultHandler(encoder);
    }

    @Bean
    public Jt808HandlerResultHandler jt808ResponseMsgBodyHandlerResultHandler(Jt808MsgEncoder encoder, Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        return new Jt808ResponseMsgBodyHandlerResultHandler(annotationBasedEncoder, encoder);
    }

    @Bean
    public Jt808DispatcherHandler jt808DispatcherHandler(
            Jt808SessionManager sessionManager,
            ObjectProvider<Jt808HandlerMapping> handlerMappings,
            ObjectProvider<Jt808HandlerAdapter> handlerAdapters,
            ObjectProvider<Jt808HandlerResultHandler> resultHandlers,
            Jt808ExceptionHandler exceptionHandler) {

        return new Jt808DispatcherHandler(
                sessionManager,
                handlerMappings.stream().collect(Collectors.toList()),
                handlerAdapters.stream().collect(Collectors.toList()),
                resultHandlers.stream().collect(Collectors.toList()),
                exceptionHandler);
    }
}
