package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;
import org.springframework.core.annotation.AnnotatedElementUtils;

public class Jt808RequestMsgBodyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final Jt808AnnotationBasedDecoder annotationBasedDecoder = new Jt808AnnotationBasedDecoder();

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return AnnotatedElementUtils.isAnnotated(methodParameter.getParameterType(), Jt808RequestMsgBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws Jt808ArgumentResolveException {
        final Class<?> parameterType = methodParameter.getParameterType();

        assert context.getRequest() != null;

        return this.annotationBasedDecoder.decode(context.getRequest(), parameterType);
    }
}
