package io.github.hylexus.jt.jt808.support.dispatcher.handler.result;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.Jt808ResponseMsgBody;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;

@Slf4j
public class Jt808ResponseMsgBodyHandlerResultHandler implements Jt808HandlerResultHandler {

    private final Jt808AnnotationBasedEncoder annotationBasedEncoder = new Jt808AnnotationBasedEncoder();

    @Override
    public boolean supports(Jt808HandlerResult handlerResult) {
        return handlerResult != null
               && handlerResult.getReturnValue() != null
               && AnnotatedElementUtils.isAnnotated(handlerResult.getReturnValue().getClass(), Jt808ResponseMsgBody.class);
    }

    @Override
    public void handleResult(Jt808Request request, Jt808Session session, Jt808HandlerResult handlerResult) {
        log.info("Porcess result: {}", handlerResult.getReturnValue());
        assert handlerResult.getReturnValue() != null;
        final ByteBuf buf = this.annotationBasedEncoder.encode(handlerResult.getReturnValue());
        try {
            session.sendMsgToClient(buf);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
