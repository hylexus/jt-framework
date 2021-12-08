package io.github.hylexus.jt.jt808.support.utils;

import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;

/**
 * @author hylexus
 */
public class ArgumentUtils {

    public static Object[] resolveArguments(HandlerMethod handlerMethod, ArgumentContext context, Jt808HandlerMethodArgumentResolver argumentResolver) {
        final Object[] args = new Object[handlerMethod.getParameters().length];

        for (int i = 0; i < handlerMethod.getParameters().length; i++) {
            final MethodParameter parameter = handlerMethod.getParameters()[i];
            args[i] = argumentResolver.resolveArgument(parameter, context);
        }
        return args;
    }

}
