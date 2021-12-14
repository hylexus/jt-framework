package io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;

/**
 * @author hylexus
 */
public class SimpleJt808RequestHandlerHandlerAdapter implements Jt808HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof SimpleJt808RequestHandler;
    }

    @Override
    public Jt808HandlerResult handle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        final SimpleJt808RequestHandler<?> reqMsgHandler = (SimpleJt808RequestHandler<?>) handler;
        final Object result = reqMsgHandler.handleMsg(exchange);
        if (result == null) {
            return Jt808HandlerResult.empty();
        }
        return Jt808HandlerResult.of(handler, result);
    }

}
