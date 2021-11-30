package io.github.hylexus.jt.jt808.support.dispatcher.mapping;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;

import java.util.Optional;
import java.util.function.Function;

public class Jt808RequestMsgHandlerAnnotationHandlerMapping implements Jt808HandlerMapping {
    private final ComponentMapping<HandlerMethod> msgHandlerComponentMapping;

    public Jt808RequestMsgHandlerAnnotationHandlerMapping(ComponentMapping<HandlerMethod> msgHandlerComponentMapping) {
        this.msgHandlerComponentMapping = msgHandlerComponentMapping;
    }

    @Override
    public Optional<Object> getHandler(Jt808Request request, Jt808Session session) {
        // TODO cache
        return msgHandlerComponentMapping.getComponent(request.msgType(), request.header().version())
                .map(Function.identity());
    }
}
