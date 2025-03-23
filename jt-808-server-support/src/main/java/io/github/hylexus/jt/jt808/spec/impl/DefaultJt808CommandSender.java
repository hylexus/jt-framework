package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.JtCommunicationException;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.spec.utils.DynamicField;
import io.github.hylexus.jt.jt808.spec.utils.DynamicFieldBasedJt808MsgEncoder;
import io.github.hylexus.jt.jt808.spec.utils.DynamicFieldEncoder;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.exception.Jt808EncodeException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 */
@BuiltinComponent
public class DefaultJt808CommandSender extends AbstractJt808CommandSender {

    private final Jt808MsgEncoder msgEncoder;
    private final Jt808AnnotationBasedEncoder annotationBasedEncoder;
    private final DynamicFieldBasedJt808MsgEncoder dynamicFieldBasedJt808MsgEncoder;

    public DefaultJt808CommandSender(Jt808SessionManager sessionManager, Jt808MsgEncoder msgEncoder, Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        super(sessionManager);
        this.msgEncoder = msgEncoder;
        this.annotationBasedEncoder = annotationBasedEncoder;
        this.dynamicFieldBasedJt808MsgEncoder = new DynamicFieldBasedJt808MsgEncoder(
                ByteBufAllocator.DEFAULT,
                msgEncoder,
                new DynamicFieldEncoder()
        );
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

    @Override
    public void sendCommand(DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<DynamicField> body) throws JtCommunicationException {
        final Jt808Session session = getSession(metadata.getTerminalId());
        final ByteBuf encoded = this.encodeDynamicFields(body, session, metadata);
        session.sendMsgToClient(encoded);
    }

    @Override
    public void sendCommandWithDynamicFields(DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<Map<String, Object>> body) throws JtCommunicationException {
        final Jt808Session session = getSession(metadata.getTerminalId());
        final ByteBuf encoded = this.encodeDynamicFieldsFromMapList(body, session, metadata);
        session.sendMsgToClient(encoded);
    }

    @Override
    public <T> T sendCommandAndWaitingForReply(Jt808CommandKey commandKey, DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<DynamicField> body, Duration timeout)
            throws JtCommunicationException, InterruptedException {
        final Jt808Session session = getSession(commandKey.terminalId());
        final ByteBuf encoded = this.encodeDynamicFields(body, session, metadata);
        return sendAndWait(commandKey, getSession(commandKey.terminalId()), timeout.toMillis(), TimeUnit.MILLISECONDS, encoded);
    }

    @Override
    public <T> T sendCommandWithDynamicFieldsAndWaitingForReply(Jt808CommandKey commandKey, DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<Map<String, Object>> body, Duration timeout)
            throws JtCommunicationException, InterruptedException {
        final Jt808Session session = getSession(commandKey.terminalId());
        final ByteBuf encoded = this.encodeDynamicFieldsFromMapList(body, session, metadata);
        return sendAndWait(commandKey, getSession(commandKey.terminalId()), timeout.toMillis(), TimeUnit.MILLISECONDS, encoded);
    }

    protected ByteBuf encodeDynamicFieldsFromMapList(List<Map<String, Object>> body, Jt808Session session, DynamicFieldBasedJt808MsgEncoder.Metadata metadata) {
        return this.dynamicFieldBasedJt808MsgEncoder.encodeWithMapList(
                new DynamicFieldBasedJt808MsgEncoder.Metadata()
                        .setVersion(session.protocolVersion())
                        .setTerminalId(session.terminalId())
                        .setMsgId(metadata.getMsgId())
                        .setEncryptionType(metadata.getEncryptionType())
                        .setMaxPackageSize(metadata.getMaxPackageSize())
                        .setReversedBit15InHeader(metadata.getReversedBit15InHeader()),
                session,
                body
        );
    }

    protected ByteBuf encodeDynamicFields(List<DynamicField> body, Jt808Session session, DynamicFieldBasedJt808MsgEncoder.Metadata metadata) {
        return this.dynamicFieldBasedJt808MsgEncoder.encodeWithDynamicFieldList(
                new DynamicFieldBasedJt808MsgEncoder.Metadata()
                        .setVersion(session.protocolVersion())
                        .setTerminalId(session.terminalId())
                        .setMsgId(metadata.getMsgId())
                        .setEncryptionType(metadata.getEncryptionType())
                        .setMaxPackageSize(metadata.getMaxPackageSize())
                        .setReversedBit15InHeader(metadata.getReversedBit15InHeader()),
                session,
                body
        );
    }

}
