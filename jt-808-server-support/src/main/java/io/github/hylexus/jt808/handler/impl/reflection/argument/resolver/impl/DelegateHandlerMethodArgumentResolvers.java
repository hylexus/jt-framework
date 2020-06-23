package io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.exception.ArgumentResolveException;
import io.github.hylexus.jt808.handler.impl.reflection.MethodParameter;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hylexus
 * Created At 2020-02-02 1:21 下午
 */
@BuiltinComponent
public class DelegateHandlerMethodArgumentResolvers implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
    private final ConcurrentMap<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>();

    public DelegateHandlerMethodArgumentResolvers() {
        addDefaultHandlerMethodArgumentResolver(this);
    }

    static void addDefaultHandlerMethodArgumentResolver(DelegateHandlerMethodArgumentResolvers resolvers) {
        resolvers.addResolver(new RequestMsgBodyArgumentResolver());
        resolvers.addResolver(new RequestMsgHeaderArgumentResolver());
        resolvers.addResolver(new RequestMsgMetadataArgumentResolver());
        resolvers.addResolver(new SessionArgumentResolver());
        resolvers.addResolver(new ExceptionArgumentResolver());
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final HandlerMethodArgumentResolver resolver = getResolver(methodParameter);
        return resolver != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ArgumentContext context) {
        final HandlerMethodArgumentResolver resolver = this.getResolver(parameter);
        if (resolver != null) {
            return resolver.resolveArgument(parameter, context);
        }
        throw new ArgumentResolveException(context);
    }

    private HandlerMethodArgumentResolver getResolver(MethodParameter methodParameter) {
        final HandlerMethodArgumentResolver resolver = this.argumentResolverCache.get(methodParameter);
        if (resolver != null) {
            return resolver;
        }

        for (HandlerMethodArgumentResolver argumentResolver : this.resolvers) {
            if (argumentResolver.supportsParameter(methodParameter)) {
                this.argumentResolverCache.put(methodParameter, argumentResolver);
                return argumentResolver;
            }
        }

        return null;
    }

    public DelegateHandlerMethodArgumentResolvers addResolver(HandlerMethodArgumentResolver resolver) {
        this.resolvers.add(resolver);
        return this;
    }
}
