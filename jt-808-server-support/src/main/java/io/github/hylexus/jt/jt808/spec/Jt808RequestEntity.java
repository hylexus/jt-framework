package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
@BuiltinComponent
public class Jt808RequestEntity<T> {
    private final MsgType msgType;
    private final Jt808RequestHeader header;
    private final ByteBuf rawByteBuf;
    private final T body;
    private final byte originalCheckSum;
    private final byte calculatedCheckSum;
    private final Jt808Session session;

    public Jt808RequestEntity(Jt808Request request, Jt808Session session, T body) {
        this.msgType = request.msgType();
        this.header = request.header();
        this.rawByteBuf = request.rawByteBuf();
        this.body = body;
        this.originalCheckSum = request.originalCheckSum();
        this.calculatedCheckSum = request.calculatedCheckSum();
        this.session = session;
    }

    public MsgType msgType() {
        return msgType;
    }

    public int msgId() {
        return msgType.getMsgId();
    }

    public Jt808RequestHeader header() {
        return header;
    }

    public ByteBuf rawByteBuf() {
        return rawByteBuf;
    }

    public T body() {
        return body;
    }

    public byte originalCheckSum() {
        return originalCheckSum;
    }

    public byte calculatedCheckSum() {
        return calculatedCheckSum;
    }

    public Jt808Session session() {
        return session;
    }

    // delegate method

    public int msgBodyStartIndex() {
        return header.msgBodyStartIndex();
    }

    public Jt808ProtocolVersion version() {
        return header.version();
    }

    public Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps() {
        return header.msgBodyProps();
    }

    public int msgBodyLength() {
        return header.msgBodyLength();
    }

    public String terminalId() {
        return header.terminalId();
    }

    public int flowId() {
        return header.flowId();
    }

}
