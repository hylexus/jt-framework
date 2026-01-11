package io.github.hylexus.jt.jt808.adapter.xtreamcodec;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.AbstractJt808HandlerResultHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseBodyHandlerResultHandler;
import io.github.hylexus.jt.utils.JtAnnotationUtils;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static java.util.Objects.requireNonNull;

public class XtreamCodecJt808ResponseBodyHandler extends AbstractJt808HandlerResultHandler {
    private final Jt808MsgEncoder encoder;
    private final Jt808AnnotationBasedEncoder annotationBasedEncoder;
    private final EntityCodec entityCodec;
    private final Jt808ResponseBodyHandlerResultHandler fallbackHandler;

    public XtreamCodecJt808ResponseBodyHandler(Jt808MsgEncoder encoder, Jt808AnnotationBasedEncoder annotationBasedEncoder, EntityCodec entityCodec, Jt808ResponseBodyHandlerResultHandler fallbackHandler) {
        this.encoder = encoder;
        this.annotationBasedEncoder = annotationBasedEncoder;
        this.entityCodec = entityCodec;
        this.fallbackHandler = fallbackHandler;
    }

    @Override
    public boolean supports(Jt808HandlerResult handlerResult) {
        return this.fallbackHandler.supports(handlerResult);
    }

    @Override
    public void handleResult(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult) {
        if (exchange.response().body().writerIndex() > 0) {
            throw new JtIllegalStateException(
                    "MsgHandler returns the entity class marked by @" + Jt808ResponseBody.class.getSimpleName()
                            + " \"AND\" modifies the response body"
            );
        }
        final Object returnValue = handlerResult.getReturnValue();
        final Jt808ResponseBody annotation = JtAnnotationUtils.getMergedAnnotation(returnValue.getClass(), Jt808ResponseBody.class);
        if (requireNonNull(annotation).drivenBy().value() == DrivenBy.Type.XTREAM_CODEC) {
            final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
            final int version = exchange.request().version().getVersionIdentifier();
            this.entityCodec.encode(version, returnValue, buffer);
            this.annotationBasedEncoder.encodeAndWriteToResponse(buffer, exchange, annotation);

            final ByteBuf respByteBuf = this.encoder.encode(exchange.response(), exchange.session());
            if (this.shouldSkipResponse(exchange, handlerResult, respByteBuf)) {
                return;
            }
            exchange.session().sendMsgToClient(respByteBuf);
        } else {
            this.fallbackHandler.handleResult(exchange, handlerResult);
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
