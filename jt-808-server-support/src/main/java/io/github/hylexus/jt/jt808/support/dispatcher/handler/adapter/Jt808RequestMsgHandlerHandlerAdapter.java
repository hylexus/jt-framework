package io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808RequestMsgHandler;

/**
 * @author hylexus
 */
public class Jt808RequestMsgHandlerHandlerAdapter implements Jt808HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Jt808RequestMsgHandler;
    }

    @Override
    public Jt808HandlerResult handle(Jt808ServerExchange exchange, Object handler) throws Throwable {
        final Jt808RequestMsgHandler<?> reqMsgHandler = (Jt808RequestMsgHandler<?>) handler;
        final Object result = reqMsgHandler.handleMsg(exchange);
        if (result == null) {
            return Jt808HandlerResult.empty();
        }
        return Jt808HandlerResult.of(handler, result);
    }

}
