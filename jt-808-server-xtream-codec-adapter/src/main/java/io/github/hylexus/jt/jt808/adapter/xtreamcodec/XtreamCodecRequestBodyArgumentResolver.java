package io.github.hylexus.jt.jt808.adapter.xtreamcodec;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import org.springframework.core.annotation.AnnotatedElementUtils;

public class XtreamCodecRequestBodyArgumentResolver implements Jt808HandlerMethodArgumentResolver {
    private final EntityCodec entityCodec;
    private final Jt808AnnotationBasedDecoder fallbackDecoder;

    public XtreamCodecRequestBodyArgumentResolver(EntityCodec entityCodec, Jt808AnnotationBasedDecoder fallbackDecoder) {
        this.fallbackDecoder = fallbackDecoder;
        this.entityCodec = entityCodec;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return AnnotatedElementUtils.isAnnotated(methodParameter.getParameterType(), Jt808RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws Jt808ArgumentResolveException {
        final Class<?> parameterType = methodParameter.getParameterType();
        final Jt808Request request = context.getExchange().request();
        if (methodParameter.drivenByXtreamCodec()) {
            final int version = request.header().version().getVersionIdentifier();
            return this.entityCodec.decode(version, parameterType, request.body());
        } else {
            return this.fallbackDecoder.decode(request, parameterType);
        }
    }

}
