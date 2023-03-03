package io.github.hylexus.jt.jt808.boot.config.configuration;

import io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher.*;
import io.github.hylexus.jt.jt808.support.dispatcher.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Import({
        HandlerMappingAutoConfiguration.class,
        HandlerAdaptorAutoConfiguration.class,
        HandlerResultHandlerAutoConfiguration.class,
        ExceptionHandlerAutoConfiguration.class,
        HandlerMethodArgumentResolverAutoConfiguration.class,
})
public class Jt808DispatcherHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jt808DispatcherHandler jt808DispatcherHandler(
            ObjectProvider<Jt808HandlerMapping> handlerMappings,
            ObjectProvider<Jt808HandlerAdapter> handlerAdapters,
            ObjectProvider<Jt808HandlerResultHandler> resultHandlers,
            Jt808ExceptionHandler exceptionHandler) {

        return new Jt808DispatcherHandler(
                handlerMappings.stream().collect(Collectors.toList()),
                handlerAdapters.stream().collect(Collectors.toList()),
                resultHandlers.stream().collect(Collectors.toList()),
                exceptionHandler);
    }
}
