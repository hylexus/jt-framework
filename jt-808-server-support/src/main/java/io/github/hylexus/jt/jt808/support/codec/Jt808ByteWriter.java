package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @author hylexus
 */
public interface Jt808ByteWriter {

    ByteBuf writable();

    static Jt808ByteWriter of(ByteBuf value) {
        return () -> value;
    }

    default Jt808ByteWriter writeBcd(String bcd) {
        JtProtocolUtils.writeBcd(writable(), bcd);
        return this;
    }

    default Jt808ByteWriter writeString(String string, Charset charset) {
        JtProtocolUtils.writeString(writable(), string, charset);
        return this;
    }

    default Jt808ByteWriter writeString(String string) {
        JtProtocolUtils.writeString(writable(), string);
        return this;
    }

    default Jt808ByteWriter writeWord(int value) {
        JtProtocolUtils.writeWord(writable(), value);
        return this;
    }

    default Jt808ByteWriter writeDWord(int value) {
        JtProtocolUtils.writeDword(writable(), value);
        return this;
    }

    default Jt808ByteWriter writeByte(int value) {
        JtProtocolUtils.writeByte(writable(), value);
        return this;
    }

    default Jt808ByteWriter writeBytes(ByteBuf byteBuf, boolean autoClose) {
        try {
            writable().writeBytes(byteBuf);
            return this;
        } finally {
            if (autoClose) {
                JtProtocolUtils.release(byteBuf);
            }
        }
    }

    default Jt808ByteWriter writeBytes(ByteBuf byteBuf) {
        return this.writeBytes(byteBuf, true);
    }

    default Jt808ByteWriter writeBytes(byte[] bytes) {
        writable().writeBytes(bytes);
        return this;
    }

    default Jt808ByteWriter clear() {
        writable().clear();
        return this;
    }
}
