package io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl;

import io.github.hylexus.jt808.exception.ArgumentResolveException;
import io.github.hylexus.jt808.handler.impl.reflection.MethodParameter;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;

/**
 * @author hylexus
 * Created At 2020-02-09 12:55 下午
 */
public class ExceptionArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Throwable.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws ArgumentResolveException {
        return context.getThrowable();
    }
}
