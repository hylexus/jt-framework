package io.github.hylexus.jt.jt808.adapter.xtreamcodec;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class XtreamCodecRequestEntityArgumentResolver implements Jt808HandlerMethodArgumentResolver {
    private final EntityCodec entityCodec;
    private final Jt808AnnotationBasedDecoder fallbackDecoder;

    public XtreamCodecRequestEntityArgumentResolver(EntityCodec entityCodec, Jt808AnnotationBasedDecoder fallbackDecoder) {
        this.entityCodec = entityCodec;
        this.fallbackDecoder = fallbackDecoder;
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

        final List<Class<?>> genericTypeList = methodParameter.getGenericType();
        if (CollectionUtils.isEmpty(genericTypeList)) {
            throw new Jt808ArgumentResolveException("Can not resolve GenericType on type [" + parameterType + "]", context);
        }
        final Class<?> paramType = genericTypeList.getFirst();

        final Object entity;
        if (methodParameter.drivenByXtreamCodec()) {
            final int version = request.header().version().getVersionIdentifier();
            entity = this.entityCodec.decode(version, paramType, request.body());
        } else {
            entity = this.fallbackDecoder.decode(request, paramType);
        }

        return new Jt808RequestEntity<>(request, exchange.session(), entity);
    }
}
