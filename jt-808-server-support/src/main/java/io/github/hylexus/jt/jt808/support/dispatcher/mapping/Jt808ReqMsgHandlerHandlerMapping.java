package io.github.hylexus.jt.jt808.support.dispatcher.mapping;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerExecutionChain;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808ReqMsgHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;

import java.util.List;
import java.util.Optional;

/**
 * @author hylexus
 */
public class Jt808ReqMsgHandlerHandlerMapping extends AbstractJt808HandlerMapping {

    private final ComponentMapping<Jt808ReqMsgHandler<?>> msgHandlerComponentMapping;

    public Jt808ReqMsgHandlerHandlerMapping(ComponentMapping<Jt808ReqMsgHandler<?>> msgHandlerComponentMapping, List<Jt808HandlerInterceptor> interceptors) {
        super(interceptors);
        this.msgHandlerComponentMapping = msgHandlerComponentMapping;
    }

    @Override
    public Optional<Jt808HandlerExecutionChain> getHandler(Jt808Request request, Jt808Session session) {
        return msgHandlerComponentMapping.getComponent(request.msgType(), request.header().version())
                .map(handler -> super.buildHandlerExecutionChain(request, session, handler));
    }

}
