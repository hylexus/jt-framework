package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

public interface Jt808ByteWriter {
    ByteBuf value();

    static Jt808ByteWriter of(ByteBuf value) {
        return () -> value;
    }

    default Jt808ByteWriter writeBcd(String bcd) {
        JtProtocolUtils.writeBcd(value(), bcd);
        return this;
    }

    default Jt808ByteWriter writeString(String string) {
        JtProtocolUtils.writeString(value(), string);
        return this;
    }

    default Jt808ByteWriter writeWord(int value) {
        JtProtocolUtils.writeWord(value(), value);
        return this;
    }

    default Jt808ByteWriter writeDWord(int value) {
        JtProtocolUtils.writeDword(value(), value);
        return this;
    }

    default Jt808ByteWriter writeByte(int value) {
        JtProtocolUtils.writeByte(value(), value);
        return this;
    }

    default Jt808ByteWriter writeBytes(ByteBuf byteBuf) {
        value().readBytes(byteBuf);
        return this;
    }

    default Jt808ByteWriter writeBytes(byte[] bytes) {
        value().readBytes(bytes);
        return this;
    }
}
