package io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher;

import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter.Jt808RequestHandlerMappingHandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter.SimpleJt808RequestHandlerHandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;

/**
 * @author hylexus
 */
public class HandlerAdaptorAutoConfiguration {

    @Bean
    public Jt808HandlerAdapter jt808ReqMsgHandlerHandlerAdapter() {
        return new SimpleJt808RequestHandlerHandlerAdapter();
    }

    @Bean
    public Jt808HandlerAdapter handlerMethodHandlerAdapter(Jt808HandlerMethodArgumentResolver argumentResolver) {
        return new Jt808RequestHandlerMappingHandlerAdapter(argumentResolver);
    }
}