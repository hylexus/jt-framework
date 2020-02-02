package io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl;

import io.github.hylexus.jt808.exception.ArgumentResolveException;
import io.github.hylexus.jt808.handler.impl.reflection.MethodParameter;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hylexus
 * Created At 2020-02-02 1:21 下午
 */
public class DelegateHandlerMethodArgumentResolvers implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
    private final ConcurrentMap<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>();

    static void addDefaultHandlerMethodArgumentResolver(DelegateHandlerMethodArgumentResolvers resolvers) {
        resolvers.addResolver(new RequestMsgBodyArgumentResolver());
        resolvers.addResolver(new RequestMsgHeaderArgumentResolver());
        resolvers.addResolver(new RequestMsgMetadataArgumentResolver());
        resolvers.addResolver(new SessionArgumentResolver());
    }

    public DelegateHandlerMethodArgumentResolvers() {
        addDefaultHandlerMethodArgumentResolver(this);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final HandlerMethodArgumentResolver resolver = getResolver(methodParameter);
        return resolver != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, RequestMsgMetadata metadata, Session session, RequestMsgBody msg) {
        HandlerMethodArgumentResolver resolver = this.getResolver(parameter);
        if (resolver != null) {
            return resolver.resolveArgument(parameter, metadata, session, msg);
        }
        throw new ArgumentResolveException(parameter, metadata, session, msg);
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
