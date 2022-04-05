package io.github.hylexus.jt.jt808.support.dispatcher.handler.result;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.SimpleJt808RequestHandlerHandlerMapping;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @author hylexus
 * @see Jt808ResponseBody
 */
@Slf4j
public class Jt808ResponseBodyHandlerResultHandler extends AbstractJt808HandlerResultHandler {

    private final Jt808AnnotationBasedEncoder annotationBasedEncoder;
    private final Jt808MsgEncoder encoder;

    public Jt808ResponseBodyHandlerResultHandler(Jt808AnnotationBasedEncoder annotationBasedEncoder, Jt808MsgEncoder encoder) {
        this.annotationBasedEncoder = annotationBasedEncoder;
        this.encoder = encoder;
    }

    @Override
    public boolean supports(Jt808HandlerResult handlerResult) {
        return handlerResult != null
               && handlerResult.getReturnValue() != null
               && AnnotatedElementUtils.isAnnotated(handlerResult.getReturnValue().getClass(), Jt808ResponseBody.class);
    }

    @Override
    public void handleResult(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult) {
        if (exchange.response().body().writerIndex() > 0) {
            throw new JtIllegalStateException(
                    "MsgHandler returns the entity class marked by @" + Jt808ResponseBody.class.getSimpleName()
                    + " \"AND\" modifies the response body"
            );
        }
        this.doHandleResult(exchange, handlerResult);
    }

    @Override
    public int getOrder() {
        return SimpleJt808RequestHandlerHandlerMapping.ORDER;
    }

    private void doHandleResult(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult) {
        final Object returnValue = handlerResult.getReturnValue();
        assert returnValue != null;

        this.annotationBasedEncoder.encodeAndWriteToResponse(returnValue, exchange);

        final ByteBuf respByteBuf = this.encoder.encode(exchange.response(), exchange.session());
        if (this.shouldSkipResponse(exchange, handlerResult, respByteBuf)) {
            return;
        }
        exchange.session().sendMsgToClient(respByteBuf);
    }
}
