package io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl.Jt808HandlerMethodArgumentResolverComposite;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.utils.ArgumentUtils;

import java.lang.reflect.InvocationTargetException;

public class HandlerMethodHandlerAdapter implements Jt808HandlerAdapter {

    private final HandlerMethodArgumentResolver argumentResolver = new Jt808HandlerMethodArgumentResolverComposite();

    public HandlerMethodHandlerAdapter() {
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public Jt808HandlerResult handle(Jt808Request request, Jt808Session session, Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        try {
            return this.invokeHandlerMethod(handlerMethod, request, session);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // TODO exception handler
        return null;
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

        final Jt808HandlerResult handlerResult = new Jt808HandlerResult().setHandler(handlerMethod).setReturnValue(result);
        if (result == null && handlerMethod.isVoidReturnType()) {
            handlerResult.setRequestProcessed(true);
        }
        return handlerResult;
    }

    private Object[] resolveArgs(HandlerMethod handlerMethod, Jt808Request request, Jt808Session session) {
        final ArgumentContext argumentContext = ArgumentContext.of(request, session, null);
        return ArgumentUtils.resolveArguments(handlerMethod, argumentContext, this.argumentResolver);
    }
}
