package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.session.Jt808Session;

import javax.annotation.Nullable;

public interface Jt808HandlerInterceptor extends OrderedComponent {

    default boolean preHandle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        return true;
    }

    default void postHandle(Jt808ServerExchange exchange, Object handler, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
    }

    default void afterCompletion(Jt808ServerExchange exchange, Object handler, @Nullable Throwable exception) throws Throwable {
    }

    @Override
    int getOrder();
}
