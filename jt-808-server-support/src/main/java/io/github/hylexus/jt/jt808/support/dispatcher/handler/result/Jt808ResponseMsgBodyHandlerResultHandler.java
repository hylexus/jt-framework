package io.github.hylexus.jt.jt808.support.dispatcher.handler.result;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.response.impl.DefaultJt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.Jt808ResponseMsgBody;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import static java.util.Objects.requireNonNull;

@Slf4j
public class Jt808ResponseMsgBodyHandlerResultHandler implements Jt808HandlerResultHandler {

    private final Jt808AnnotationBasedEncoder annotationBasedEncoder = new Jt808AnnotationBasedEncoder();
    private final Jt808MsgEncoder encoder;

    public Jt808ResponseMsgBodyHandlerResultHandler(Jt808MsgEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public boolean supports(Jt808HandlerResult handlerResult) {
        return handlerResult != null
               && handlerResult.getReturnValue() != null
               && AnnotatedElementUtils.isAnnotated(handlerResult.getReturnValue().getClass(), Jt808ResponseMsgBody.class);
    }

    @Override
    public void handleResult(Jt808Request request, Jt808Session session, Jt808HandlerResult handlerResult) {
        final Object returnValue = handlerResult.getReturnValue();
        assert returnValue != null;

        final Class<?> entityClass = returnValue.getClass();
        // TODO annotation properties...
        final Jt808ResponseMsgBody annotation = requireNonNull(
                AnnotationUtils.findAnnotation(entityClass, Jt808ResponseMsgBody.class),
                entityClass.getSimpleName() + " must be annotated with " + Jt808ResponseMsgBody.class);

        final ByteBuf respBody = this.annotationBasedEncoder.encode(returnValue);
        final DefaultJt808Response jt808Response = Jt808Response.newBuilder()
                .body(respBody)
                .version(request.version())
                .msgId(annotation.respMsgId())
                .terminalId(session.getTerminalId())
                .flowId(session.getCurrentFlowId())
                .build();
        final ByteBuf respByteBuf = this.encoder.encode(jt808Response);
        try {
            session.sendMsgToClient(respByteBuf);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO exception handler
        }
    }
}
