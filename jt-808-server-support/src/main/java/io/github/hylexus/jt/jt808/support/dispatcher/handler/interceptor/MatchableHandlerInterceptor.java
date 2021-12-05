package io.github.hylexus.jt.jt808.support.dispatcher.handler.interceptor;

import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.interceptor.Jt808HandlerInterceptorMatcher;

/**
 * @author hylexus
 */
public interface MatchableHandlerInterceptor extends Jt808HandlerInterceptor {
    Jt808HandlerInterceptorMatcher requestMatcher();
}
