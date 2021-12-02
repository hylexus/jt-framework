package io.github.hylexus.jt.jt808.request.impl;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeaderSpec;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public class DefaultJt808Request implements Jt808Request {

    protected final Jt808MsgHeaderSpec header;
    protected final ByteBuf rawByteBuf;
    protected final byte originalCheckSum;
    protected final byte calculatedCheckSum;
    protected final MsgType msgType;
    private final Map<String, Object> attributes;

    public DefaultJt808Request(Jt808MsgHeaderSpec header, ByteBuf rawByteBuf, byte originalCheckSum, byte calculatedCheckSum, MsgType msgType) {
        this.header = header;
        this.rawByteBuf = rawByteBuf;
        this.originalCheckSum = originalCheckSum;
        this.calculatedCheckSum = calculatedCheckSum;
        this.msgType = msgType;
        this.attributes = new HashMap<>();
    }

    @Override
    public Jt808MsgHeaderSpec header() {
        return header;
    }

    @Override
    public ByteBuf rawByteBuf() {
        return rawByteBuf;
    }

    @Override
    public ByteBuf body() {
        return rawByteBuf.slice(header.msgBodyStartIndex(), header.msgBodyLength());
    }

    @Override
    public byte originalCheckSum() {
        return originalCheckSum;
    }

    @Override
    public byte calculatedCheckSum() {
        return calculatedCheckSum;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public MsgType msgType() {
        return msgType;
    }

    @Override
    public String toString() {
        return "DefaultJt808Request{"
               + "msgType=" + msgType
               + ", header=" + header
               + ", checkSum=" + originalCheckSum
               + '}';
    }
}
