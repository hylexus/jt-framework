package io.github.hylexus.jt.jt808.boot.config.configuration;

import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.converter.MsgTypeParser;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.stream.Collectors;

/**
 * @author hylexus
 */
public class Jt808DispatcherHandlerAutoConfiguration {

    @Bean
    public DelegateExceptionHandler delegateExceptionHandler() {
        return new DelegateExceptionHandler().addExceptionHandler(new BuiltinLoggingOnlyExceptionHandler());
    }

    @Bean
    public Jt808HandlerMapping jt808ReqMsgHandlerHandlerMapping(
            ObjectProvider<Jt808ReqMsgHandler<?>> jt808ReqMsgHandlers) {

        final ComponentMapping<Jt808ReqMsgHandler<?>> componentMapping = new ComponentMapping<>();
        jt808ReqMsgHandlers.stream().forEach(componentMapping::register);
        return new Jt808ReqMsgHandlerHandlerMapping(componentMapping);
    }

    @Bean
    public Jt808HandlerMapping jt808HandlerMapping(ApplicationContext applicationContext, MsgTypeParser msgTypeParser) {
        final ComponentMapping<HandlerMethod> msgHandlerComponentMapping = new ComponentMapping<>();
        new Jt808RequestMsgHandlerScanner(applicationContext, msgTypeParser)
                .doScan(msgHandlerComponentMapping);
        return new Jt808RequestMsgHandlerAnnotationHandlerMapping(msgHandlerComponentMapping);
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
    public Jt808HandlerResultHandler jt808ResponseMsgBodyHandlerResultHandler() {
        return new Jt808ResponseMsgBodyHandlerResultHandler();
    }

    @Bean
    public Jt808DispatcherHandler jt808DispatcherHandler(
            Jt808SessionManager sessionManager,
            ObjectProvider<Jt808HandlerMapping> handlerMappings,
            ObjectProvider<Jt808HandlerAdapter> handlerAdapters,
            ObjectProvider<Jt808HandlerResultHandler> resultHandlers,
            DelegateExceptionHandler exceptionHandler) {

        return new Jt808DispatcherHandler(
                sessionManager,
                handlerMappings.orderedStream().collect(Collectors.toList()),
                handlerAdapters.orderedStream().collect(Collectors.toList()),
                resultHandlers.orderedStream().collect(Collectors.toList()),
                exceptionHandler);
    }
}
