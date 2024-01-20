package io.github.hylexus.jt.jt808.spec.impl.request;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.MutableJt808Request;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public class DefaultJt808Request implements MutableJt808Request {
    protected Jt808Session session;
    protected final Jt808RequestHeader header;
    protected final ByteBuf rawByteBuf;
    protected final ByteBuf body;
    protected final byte originalCheckSum;
    protected final byte calculatedCheckSum;
    protected final MsgType msgType;
    private final Map<String, Object> attributes;

    public DefaultJt808Request(
            MsgType msgType, Jt808RequestHeader header, ByteBuf rawByteBuf, ByteBuf body,
            byte originalCheckSum, byte calculatedCheckSum) {
        this(null, msgType, header, rawByteBuf, body, originalCheckSum, calculatedCheckSum);
    }

    public DefaultJt808Request(
            Jt808Session session,
            MsgType msgType, Jt808RequestHeader header, ByteBuf rawByteBuf, ByteBuf body,
            byte originalCheckSum, byte calculatedCheckSum) {
        this.session = session;
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
    public Jt808RequestHeader header() {
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
    public Jt808Session session() {
        return this.session;
    }

    @Override
    public Jt808Request session(Jt808Session session) {
        this.session = session;
        return this;
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
        private Jt808RequestHeader header;
        private Jt808Session session;
        private ByteBuf rawByteBuf;
        private ByteBuf body;
        private byte originalCheckSum;
        private byte calculatedCheckSum;
        private final MsgType msgType;

        public DefaultJt808RequestBuilder(MsgType msgType) {
            this.msgType = msgType;
        }

        public DefaultJt808RequestBuilder(Jt808Request request) {
            this.session = request.session();
            this.header = request.header();
            this.rawByteBuf = request.rawByteBuf();
            this.body = request.body();
            this.originalCheckSum = request.originalCheckSum();
            this.calculatedCheckSum = request.calculatedCheckSum();
            this.msgType = request.msgType();
        }

        @Override
        public Jt808RequestBuilder header(Jt808RequestHeader header) {
            this.header = header;
            return this;
        }

        @Override
        public Jt808RequestBuilder session(Jt808Session session) {
            this.session = session;
            return this;
        }

        @Override
        public Jt808RequestBuilder rawByteBuf(ByteBuf byteBuf, boolean autoRelease) {
            final ByteBuf old = this.rawByteBuf;
            try {
                this.rawByteBuf = byteBuf;
                return this;
            } finally {
                if (autoRelease) {
                    JtProtocolUtils.release(old);
                }
            }
        }

        @Override
        public Jt808RequestBuilder body(ByteBuf body, boolean autoRelease) {
            final ByteBuf old = this.body;
            try {
                this.body = body;
                return this;
            } finally {
                if (autoRelease) {
                    JtProtocolUtils.release(old);
                }
            }
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
            return new DefaultJt808Request(session, msgType, header, rawByteBuf, body, originalCheckSum, calculatedCheckSum);
        }
    }
}
