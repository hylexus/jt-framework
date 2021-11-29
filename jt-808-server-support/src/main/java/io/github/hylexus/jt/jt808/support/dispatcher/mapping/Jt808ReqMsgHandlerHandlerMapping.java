package io.github.hylexus.jt.jt808.support.dispatcher.mapping;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808ReqMsgHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author hylexus
 */
public class Jt808ReqMsgHandlerHandlerMapping implements Jt808HandlerMapping {

    private final ComponentMapping<Jt808ReqMsgHandler<?>> msgHandlerComponentMapping;

    public Jt808ReqMsgHandlerHandlerMapping(ComponentMapping<Jt808ReqMsgHandler<?>> msgHandlerComponentMapping) {
        this.msgHandlerComponentMapping = msgHandlerComponentMapping;
    }

    @Override
    public Optional<Object> getHandler(Jt808Request request, Jt808Session session) {
        // TODO cache
        return msgHandlerComponentMapping.getComponent(request.msgType(), request.header().version())
                .map(Function.identity());
    }

}
