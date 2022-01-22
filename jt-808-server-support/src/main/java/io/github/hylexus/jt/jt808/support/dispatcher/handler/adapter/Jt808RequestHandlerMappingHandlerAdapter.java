package io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.SimpleJt808RequestHandlerHandlerMapping;
import io.github.hylexus.jt.jt808.support.utils.ArgumentUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author hylexus
 * @see Jt808RequestHandlerMapping
 */
public class Jt808RequestHandlerMappingHandlerAdapter implements Jt808HandlerAdapter {

    private final Jt808HandlerMethodArgumentResolver argumentResolver;

    public Jt808RequestHandlerMappingHandlerAdapter(Jt808HandlerMethodArgumentResolver argumentResolver) {
        this.argumentResolver = argumentResolver;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public Jt808HandlerResult handle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Object[] args = this.resolveArgs(handlerMethod, exchange);
        return doInvoke(exchange, handlerMethod, args);
    }

    private Jt808HandlerResult doInvoke(Jt808ServerExchange exchange, HandlerMethod handlerMethod, Object[] args) throws Throwable {
        final Object result;
        try {
            result = handlerMethod.getMethod().invoke(handlerMethod.getBeanInstance(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

        if (result == null || handlerMethod.isVoidReturnType()) {
            return Jt808HandlerResult.empty();
        }
        return Jt808HandlerResult.of(handlerMethod, result);
    }

    private Object[] resolveArgs(HandlerMethod handlerMethod, Jt808ServerExchange exchange) {
        final ArgumentContext argumentContext = ArgumentContext.of(exchange, null);
        return ArgumentUtils.resolveArguments(handlerMethod, argumentContext, this.argumentResolver);
    }

    @Override
    public int getOrder() {
        return SimpleJt808RequestHandlerHandlerMapping.ORDER + 100;
    }
}
