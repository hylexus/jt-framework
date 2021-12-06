package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.utils.JtProtocolUtils;
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
        return JtProtocolUtils.getWord(buf, startIndex);
    }

    public Jt808ByteBuf writeWord(int value) {
        JtProtocolUtils.writeWord(buf, value);
        return this;
    }

    public int getDword(int startIndex) {
        return JtProtocolUtils.getDword(buf, startIndex);
    }

    public Jt808ByteBuf writeDword(int value) {
        JtProtocolUtils.writeDword(buf, value);
        return this;
    }

    public String getString(int startIndex, int length) {
        return JtProtocolUtils.getString(buf, startIndex, length);
    }

    public String getString(int startIndex, int length, Charset charset) {
        return JtProtocolUtils.getString(buf, startIndex, length, charset);
    }

    public Jt808ByteBuf writeString(String value, Charset charset) {
        JtProtocolUtils.writeString(buf, value, charset);
        return this;
    }

    public Jt808ByteBuf writeString(String value) {
        JtProtocolUtils.writeString(buf, value, JtProtocolConstant.JT_808_STRING_ENCODING);
        return this;
    }

    public String getBcd(int startIndex, int length) {
        return JtProtocolUtils.getBcd(buf, startIndex, length);
    }

    public Jt808ByteBuf writeBcd(String value) {
        JtProtocolUtils.writeBcd(buf, value);
        return this;
    }

    @Override
    public Jt808ByteBuf writeByte(int value) {
        buf.writeByte(value);
        return this;
    }

}
