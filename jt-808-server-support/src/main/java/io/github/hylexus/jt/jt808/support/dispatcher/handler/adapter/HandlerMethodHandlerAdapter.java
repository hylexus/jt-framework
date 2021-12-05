package io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.utils.ArgumentUtils;

import java.lang.reflect.InvocationTargetException;

public class HandlerMethodHandlerAdapter implements Jt808HandlerAdapter {

    private final HandlerMethodArgumentResolver argumentResolver;

    public HandlerMethodHandlerAdapter(HandlerMethodArgumentResolver argumentResolver) {
        this.argumentResolver = argumentResolver;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public Jt808HandlerResult handle(Jt808Request request, Jt808Session session, Object handler) throws Throwable {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        return this.invokeHandlerMethod(handlerMethod, request, session);
    }

    private Jt808HandlerResult invokeHandlerMethod(HandlerMethod handlerMethod, Jt808Request request, Jt808Session session) throws Throwable {
        final Object[] args = this.resolveArgs(handlerMethod, request, session);
        return doInvoke(handlerMethod, args);
    }

    private Jt808HandlerResult doInvoke(HandlerMethod handlerMethod, Object[] args) throws Throwable {
        final Object result;
        try {
            result = handlerMethod.getMethod().invoke(handlerMethod.getBeanInstance(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

        if (result == null && handlerMethod.isVoidReturnType()) {
            return Jt808HandlerResult.empty();
        }
        return new Jt808HandlerResult().setHandler(handlerMethod).setReturnValue(result);
    }

    private Object[] resolveArgs(HandlerMethod handlerMethod, Jt808Request request, Jt808Session session) {
        final ArgumentContext argumentContext = ArgumentContext.of(request, session, null);
        return ArgumentUtils.resolveArguments(handlerMethod, argumentContext, this.argumentResolver);
    }
}
