package io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl;

import io.github.hylexus.jt808.exception.ArgumentResolveException;
import io.github.hylexus.jt808.handler.impl.reflection.MethodParameter;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;

/**
 * @author hylexus
 * Created At 2020-02-02 12:42 下午
 */
public class RequestMsgMetadataArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return RequestMsgMetadata.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws ArgumentResolveException {
        return context.getMetadata();
    }
}
