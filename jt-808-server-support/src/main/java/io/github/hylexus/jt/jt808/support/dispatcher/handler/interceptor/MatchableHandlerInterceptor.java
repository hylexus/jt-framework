package io.github.hylexus.jt.jt808.support.dispatcher.handler.interceptor;

import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;

/**
 * @author hylexus
 */
public interface MatchableHandlerInterceptor extends Jt808HandlerInterceptor {
    Jt808HandlerInterceptorMatcher requestMatcher();
}
