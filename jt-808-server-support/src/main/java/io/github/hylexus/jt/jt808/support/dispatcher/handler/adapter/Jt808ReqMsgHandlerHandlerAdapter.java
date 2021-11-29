package io.github.hylexus.jt.jt808.support.dispatcher.handler.adapter;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerAdapter;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808ReqMsgHandler;

/**
 * @author hylexus
 */
public class Jt808ReqMsgHandlerHandlerAdapter implements Jt808HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Jt808ReqMsgHandler;
    }

    @Override
    public Jt808HandlerResult handle(Jt808Request request, Jt808Session session, Object handler) {
        final Jt808ReqMsgHandler<?> reqMsgHandler = (Jt808ReqMsgHandler<?>) handler;
        final Object result = reqMsgHandler.handleMsg(request, session);
        return new Jt808HandlerResult().setHandler(handler).setReturnValue(result);
    }
}
