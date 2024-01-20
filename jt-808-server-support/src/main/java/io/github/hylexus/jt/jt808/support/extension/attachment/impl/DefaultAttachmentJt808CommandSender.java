package io.github.hylexus.jt.jt808.support.extension.attachment.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.impl.AbstractJt808CommandSender;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.exception.Jt808EncodeException;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808CommandSender;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808SessionManager;
import io.netty.buffer.ByteBuf;

public class DefaultAttachmentJt808CommandSender extends AbstractJt808CommandSender implements AttachmentJt808CommandSender {
    private final Jt808MsgEncoder msgEncoder;
    private final Jt808AnnotationBasedEncoder annotationBasedEncoder;

    public DefaultAttachmentJt808CommandSender(AttachmentJt808SessionManager sessionManager, Jt808MsgEncoder msgEncoder, Jt808AnnotationBasedEncoder annotationBasedEncoder) {
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
