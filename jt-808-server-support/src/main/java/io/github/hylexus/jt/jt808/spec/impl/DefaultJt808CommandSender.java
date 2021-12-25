package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.exception.Jt808EncodeException;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
@BuiltinComponent
public class DefaultJt808CommandSender extends AbstractJt808CommandSender {

    private final Jt808MsgEncoder msgEncoder;
    private final Jt808AnnotationBasedEncoder annotationBasedEncoder;

    public DefaultJt808CommandSender(Jt808SessionManager sessionManager, Jt808MsgEncoder msgEncoder, Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        super(sessionManager);
        this.msgEncoder = msgEncoder;
        this.annotationBasedEncoder = annotationBasedEncoder;
    }

    @Override
    protected ByteBuf encode(Jt808Session session, Jt808Response response) throws Jt808EncodeException {
        return this.msgEncoder.encode(response, session);
    }

    @Override
    protected ByteBuf encode(Jt808Session session, Object response, int serverFlowId) throws Jt808EncodeException {
        final Jt808Response jt808Response = this.annotationBasedEncoder.encode(response, session, serverFlowId);
        return this.encode(session, jt808Response);
    }
}
