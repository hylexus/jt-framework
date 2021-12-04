package io.github.hylexus.jt.jt808.support.dispatcher.mapping;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerExecutionChain;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerMapping;

import java.util.List;

/**
 * @author hylexus
 */
public abstract class AbstractJt808HandlerMapping implements Jt808HandlerMapping {

    private final List<Jt808HandlerInterceptor> interceptorList;

    public AbstractJt808HandlerMapping(List<Jt808HandlerInterceptor> interceptorList) {
        this.interceptorList = interceptorList;
    }

    protected Jt808HandlerExecutionChain buildHandlerExecutionChain(Jt808Request request, Jt808Session session, Object handler) {
        final List<Jt808HandlerInterceptor> filteredInterceptor = doMatch(request, session, handler);
        return new Jt808HandlerExecutionChain(handler, filteredInterceptor);
    }

    protected List<Jt808HandlerInterceptor> doMatch(Jt808Request request, Jt808Session session, Object handler) {
        return this.interceptorList;
    }
}
