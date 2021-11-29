package io.github.hylexus.jt.jt808.request.impl;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBodySpec;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeaderSpec;

/**
 * @author hylexus
 */
public class DefaultJt808Request implements Jt808Request {

    protected final Jt808MsgHeaderSpec header;
    protected final Jt808MsgBodySpec body;
    protected final byte originalCheckSum;
    protected final byte calculatedCheckSum;
    protected final MsgType msgType;

    public DefaultJt808Request(Jt808MsgHeaderSpec header, Jt808MsgBodySpec body, byte originalCheckSum, byte calculatedCheckSum, MsgType msgType) {
        this.header = header;
        this.body = body;
        this.originalCheckSum = originalCheckSum;
        this.calculatedCheckSum = calculatedCheckSum;
        this.msgType = msgType;
    }

    @Override
    public Jt808MsgHeaderSpec header() {
        return header;
    }

    @Override
    public Jt808MsgBodySpec bodySpec() {
        return body;
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
    public MsgType msgType() {
        return msgType;
    }

    @Override
    public String toString() {
        return "RequestMsgMetadata{"
               + "msgType=" + msgType
               + ", header=" + header
               + ", checkSum=" + originalCheckSum
               + '}';
    }
}
