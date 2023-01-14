package io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher;

import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler.BuiltinLoggingOnlyExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler.CompositeJt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.scan.Jt808ExceptionHandlerScanner;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
public class ExceptionHandlerAutoConfiguration {

    @Bean
    public Jt808ExceptionHandler builtinLoggingOnlyExceptionHandler() {
        return new BuiltinLoggingOnlyExceptionHandler();
    }

    @Bean
    @Primary
    public Jt808ExceptionHandler delegateExceptionHandler(ObjectProvider<Jt808ExceptionHandler> exceptionHandlers) {
        final List<Jt808ExceptionHandler> filteredHandlers = exceptionHandlers.stream()
                .filter(e -> e.getClass() != CompositeJt808ExceptionHandler.class)
                .collect(Collectors.toList());
        return new CompositeJt808ExceptionHandler(filteredHandlers);
    }

    @Bean
    public Jt808ExceptionHandlerScanner jt808ExceptionHandlerScanner(
            Jt808ExceptionHandler exceptionHandler, Jt808HandlerMethodArgumentResolver argumentResolver) {
        return new Jt808ExceptionHandlerScanner((CompositeJt808ExceptionHandler) exceptionHandler, argumentResolver);
    }

}
