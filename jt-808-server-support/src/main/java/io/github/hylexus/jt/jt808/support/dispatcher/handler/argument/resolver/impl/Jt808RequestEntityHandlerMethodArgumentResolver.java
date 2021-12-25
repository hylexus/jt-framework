package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class Jt808RequestEntityHandlerMethodArgumentResolver implements Jt808HandlerMethodArgumentResolver {

    private final Jt808AnnotationBasedDecoder annotationBasedDecoder;

    public Jt808RequestEntityHandlerMethodArgumentResolver(Jt808AnnotationBasedDecoder annotationBasedDecoder) {
        this.annotationBasedDecoder = annotationBasedDecoder;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Jt808RequestEntity.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws Jt808ArgumentResolveException {
        final Class<?> parameterType = methodParameter.getParameterType();

        final Jt808ServerExchange exchange = context.getExchange();
        final Jt808Request request = exchange.request();
        assert request != null;

        final List<Class<?>> genericTypeList = methodParameter.getGenericType();
        if (CollectionUtils.isEmpty(genericTypeList)) {
            throw new Jt808ArgumentResolveException("Can not resolve GenericType on type [" + parameterType + "]", context);
        }
        final Class<?> genericType = genericTypeList.get(0);
        final Object entity = this.annotationBasedDecoder.decode(request, genericType);
        return new Jt808RequestEntity<>(request, exchange.session(), entity);
    }
}
