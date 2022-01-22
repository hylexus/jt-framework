package io.github.hylexus.jt.jt808.support.dispatcher.mapping;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerExecutionChain;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerInterceptor;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;

import java.util.List;
import java.util.Optional;

/**
 * @author hylexus
 * @see SimpleJt808RequestHandler
 */
public class SimpleJt808RequestHandlerHandlerMapping extends AbstractJt808HandlerMapping {
    public static final int ORDER = 0;
    private final ComponentMapping<SimpleJt808RequestHandler<?>> msgHandlerComponentMapping;

    public SimpleJt808RequestHandlerHandlerMapping(
            ComponentMapping<SimpleJt808RequestHandler<?>> msgHandlerComponentMapping,
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

    @Override
    public int getOrder() {
        return ORDER;
    }
}
