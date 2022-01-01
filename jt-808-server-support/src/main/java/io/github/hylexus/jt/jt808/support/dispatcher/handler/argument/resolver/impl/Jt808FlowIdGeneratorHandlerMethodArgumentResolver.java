package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl;

import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;

public class Jt808FlowIdGeneratorHandlerMethodArgumentResolver implements Jt808HandlerMethodArgumentResolver {

    public Jt808FlowIdGeneratorHandlerMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Jt808FlowIdGenerator.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws Jt808ArgumentResolveException {
        return context.getExchange().session();
    }
}
