package io.github.hylexus.jt.jt808.support.dispatcher.handler.result;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class Jt808ResponseHandlerResultHandler implements Jt808HandlerResultHandler {

    private final Jt808MsgEncoder encoder;

    public Jt808ResponseHandlerResultHandler(Jt808MsgEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public boolean supports(Jt808HandlerResult handlerResult) {
        return handlerResult != null && handlerResult.getReturnValue() instanceof Jt808Response;
    }

    @Override
    public void handleResult(Jt808Request request, Jt808Session session, Jt808HandlerResult handlerResult) {
        final Jt808Response returnValue = (Jt808Response) handlerResult.getReturnValue();
        final ByteBuf byteBuf = this.encoder.encode(returnValue);
        session.sendMsgToClient(byteBuf);
    }
}
