package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.impl;

import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CompositeJt808HandlerMethodArgumentResolver implements Jt808HandlerMethodArgumentResolver {

    private final List<Jt808HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
    private final ConcurrentMap<MethodParameter, Jt808HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>();

    public CompositeJt808HandlerMethodArgumentResolver(List<Jt808HandlerMethodArgumentResolver> resolverList) {
        resolverList.forEach(this::addResolver);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final Jt808HandlerMethodArgumentResolver resolver = getResolver(methodParameter);
        return resolver != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ArgumentContext context) {
        final Jt808HandlerMethodArgumentResolver resolver = this.getResolver(parameter);
        if (resolver != null) {
            return resolver.resolveArgument(parameter, context);
        }
        throw new Jt808ArgumentResolveException("Can not resolve argument [ " + parameter.getParameterType() + " ]", context);
    }

    private Jt808HandlerMethodArgumentResolver getResolver(MethodParameter methodParameter) {
        final Jt808HandlerMethodArgumentResolver resolver = this.argumentResolverCache.get(methodParameter);
        if (resolver != null) {
            return resolver;
        }

        for (Jt808HandlerMethodArgumentResolver argumentResolver : this.resolvers) {
            if (argumentResolver.supportsParameter(methodParameter)) {
                this.argumentResolverCache.put(methodParameter, argumentResolver);
                return argumentResolver;
            }
        }

        return null;
    }

    public void addResolver(Jt808HandlerMethodArgumentResolver resolver) {
        this.resolvers.add(resolver);
    }
}
