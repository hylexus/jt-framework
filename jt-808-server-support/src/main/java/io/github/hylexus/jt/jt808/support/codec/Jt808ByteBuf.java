package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.oaks.utils.BcdOps;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @author hylexus
 */
public class Jt808ByteBuf extends AbstractJt808ByteBuf {

    public static Jt808ByteBuf from(ByteBuf byteBuf) {
        if (byteBuf instanceof Jt808ByteBuf) {
            return (Jt808ByteBuf) byteBuf;
        }
        return new Jt808ByteBuf(byteBuf);
    }

    public Jt808ByteBuf(ByteBuf buf) {
        super(buf);
    }

    public int getWord(int startIndex) {
        return this.getUnsignedShort(startIndex);
    }

    public Jt808ByteBuf writeWord(int value) {
        this.writeShort(value);
        return this;
    }

    public int getDword(int startIndex) {
        return this.getInt(startIndex);
    }

    public Jt808ByteBuf writeDword(int value) {
        this.writeInt(value);
        return this;
    }

    public String getString(int startIndex, int length) {
        return getString(startIndex, length, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public String getString(int startIndex, int length, Charset charset) {
        final byte[] bytes = getBytesInternal(startIndex, length);
        return new String(bytes, charset);
    }

    public Jt808ByteBuf writeString(String value, Charset charset) {
        this.writeBytes(value.getBytes(charset));
        return this;
    }

    public Jt808ByteBuf writeString(String value) {
        return this.writeString(value, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public String getBcd(int startIndex, int length) {
        final byte[] bytes = getBytesInternal(startIndex, length);
        return BcdOps.bcd2StringV2(bytes);
    }

    public Jt808ByteBuf writeBcd(String value) {
        final byte[] bytes = BcdOps.string2Bcd(value);
        this.writeBytes(bytes);
        return this;
    }

    private byte[] getBytesInternal(int startIndex, int length) {
        final byte[] bytes = new byte[length];
        this.getBytes(startIndex, bytes, 0, length);
        return bytes;
    }

}
