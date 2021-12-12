package io.github.hylexus.jt.jt808.support.dispatcher.mapping;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerExecutionChain;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;

import java.util.List;
import java.util.Optional;

public class Jt808RequestMsgHandlerAnnotationHandlerMapping extends AbstractJt808HandlerMapping {
    private final ComponentMapping<HandlerMethod> msgHandlerComponentMapping;

    public Jt808RequestMsgHandlerAnnotationHandlerMapping(
            ComponentMapping<HandlerMethod> msgHandlerComponentMapping,
            List<Jt808HandlerInterceptor> interceptors) {

        super(interceptors);
        this.msgHandlerComponentMapping = msgHandlerComponentMapping;
    }

    @Override
    public Optional<Jt808HandlerExecutionChain> getHandler(Jt808ServerExchange exchange) {
        final Jt808Request request = exchange.request();
        final Jt808Session session = exchange.session();
        return msgHandlerComponentMapping.getComponent(request.msgType(), request.header().version())
                .map(handler -> super.buildHandlerExecutionChain(request, session, handler));
    }
}
