package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 从 SpringMvc 的 HandlerExecutionChain 复制的代码。
 */
@Slf4j
public class Jt808HandlerExecutionChain {

    private final Object handler;

    private final List<Jt808HandlerInterceptor> interceptors;

    private int interceptorIndex = -1;

    public Jt808HandlerExecutionChain(Object handler, List<Jt808HandlerInterceptor> interceptors) {
        this.handler = handler;
        this.interceptors = interceptors;
    }

    public Object getHandler() {
        return this.handler;
    }

    public List<Jt808HandlerInterceptor> getInterceptors() {
        return this.interceptors;
    }

    public boolean applyPreHandle(Jt808ServerExchange exchange) throws Throwable {
        for (int i = 0; i < interceptors.size(); i++) {
            final Jt808HandlerInterceptor interceptor = interceptors.get(i);
            if (!interceptor.preHandle(exchange, this.handler)) {
                this.triggerAfterCompletion(exchange, null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }

    public void applyPostHandle(Jt808ServerExchange exchange, @Nullable Jt808HandlerResult handlerResult) throws Throwable {
        if (interceptors.isEmpty()) {
            return;
        }
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            Jt808HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(exchange, this.handler, handlerResult);
        }
    }

    public void triggerAfterCompletion(Jt808ServerExchange exchange, @Nullable Throwable throwable) throws Throwable {
        for (int i = this.interceptorIndex; i >= 0; i--) {
            final Jt808HandlerInterceptor interceptor = interceptors.get(i);
            try {
                interceptor.afterCompletion(exchange, this.handler, throwable);
            } catch (Throwable throwable1) {
                log.error("Jt808HandlerInterceptor.afterCompletion threw exception", throwable1);
            }
        }
    }
}
