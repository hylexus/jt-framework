package io.github.hylexus.jt.jt808.request.impl;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public class DefaultJt808Request implements Jt808Request {

    protected final Jt808MsgHeader header;
    protected final ByteBuf rawByteBuf;
    protected final ByteBuf body;
    protected final byte originalCheckSum;
    protected final byte calculatedCheckSum;
    protected final MsgType msgType;
    private final Map<String, Object> attributes;

    public DefaultJt808Request(
            MsgType msgType, Jt808MsgHeader header, ByteBuf rawByteBuf, ByteBuf body,
            byte originalCheckSum, byte calculatedCheckSum) {
        this.header = header;
        this.rawByteBuf = rawByteBuf;
        this.body = (body == null)
                ? this.rawByteBuf.slice(header.msgBodyStartIndex(), header.msgBodyLength())
                : body;
        this.originalCheckSum = originalCheckSum;
        this.calculatedCheckSum = calculatedCheckSum;
        this.msgType = msgType;
        this.attributes = new HashMap<>();
    }

    @Override
    public Jt808MsgHeader header() {
        return header;
    }

    @Override
    public ByteBuf rawByteBuf() {
        return rawByteBuf;
    }

    @Override
    public ByteBuf body() {
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

    public static class DefaultJt808RequestBuilder implements Jt808RequestBuilder {
        private Jt808MsgHeader header;
        private ByteBuf rawByteBuf;
        private ByteBuf body;
        private byte originalCheckSum;
        private byte calculatedCheckSum;
        private final MsgType msgType;

        public DefaultJt808RequestBuilder(Jt808Request request) {
            this.header = request.header();
            this.rawByteBuf = request.rawByteBuf();
            this.body = request.body();
            this.originalCheckSum = request.originalCheckSum();
            this.calculatedCheckSum = request.calculatedCheckSum();
            this.msgType = request.msgType();
        }

        @Override
        public Jt808RequestBuilder header(Jt808MsgHeader header) {
            this.header = header;
            return this;
        }

        @Override
        public Jt808RequestBuilder rawByteBuf(ByteBuf byteBuf) {
            this.rawByteBuf = byteBuf;
            return this;
        }

        @Override
        public Jt808RequestBuilder body(ByteBuf body) {
            this.body = body;
            return this;
        }

        @Override
        public Jt808RequestBuilder originalCheckSum(byte checkSum) {
            this.originalCheckSum = checkSum;
            return this;
        }

        @Override
        public Jt808RequestBuilder calculatedCheckSum(byte checkSum) {
            this.calculatedCheckSum = checkSum;
            return this;
        }

        @Override
        public Jt808Request build() {
            return new DefaultJt808Request(msgType, header, rawByteBuf, body, originalCheckSum, calculatedCheckSum);
        }
    }
}
