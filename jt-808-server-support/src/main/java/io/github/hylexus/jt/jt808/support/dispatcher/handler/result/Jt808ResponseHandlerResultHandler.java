package io.github.hylexus.jt.jt808.support.dispatcher.handler.result;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 * @see Jt808Response
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
    public void handleResult(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult) {
        final Jt808Response returnValue = (Jt808Response) handlerResult.getReturnValue();
        if (returnValue != exchange.response()) {
            if (exchange.response().body().writerIndex() > 0) {
                throw new JtIllegalStateException(
                        "MsgHandler returns a new [" + Jt808Response.class.getSimpleName()
                        + "] instance \"AND\" modifies the original response body"
                );
            }

            JtProtocolUtils.release(exchange.response().body());

            final ByteBuf byteBuf = this.encoder.encode(returnValue);
            exchange.session().sendMsgToClient(byteBuf);
            return;
        }
        final ByteBuf byteBuf = this.encoder.encode(exchange.response());
        exchange.session().sendMsgToClient(byteBuf);
    }
}
