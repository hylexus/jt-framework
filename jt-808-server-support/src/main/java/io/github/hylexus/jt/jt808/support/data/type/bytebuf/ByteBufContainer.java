package io.github.hylexus.jt.jt808.support.data.type.bytebuf;

import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.support.data.type.BytesContainer;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * Created At 2019-10-17 10:46 下午
 *
 * @author hylexus
 */
public interface ByteBufContainer extends BytesContainer {

    static ByteBufContainer ofByte(byte b) {
        return new AbstractByteBufContainer.ByteByteBufContainer(b);
    }

    static ByteBufContainer ofByte(short b) {
        return new AbstractByteBufContainer.ByteByteBufContainer(b);
    }

    static ByteBufContainer ofBytes(byte[] bytes) {
        return new AbstractByteBufContainer.ByteArrayByteBufContainer(bytes);
    }

    static ByteBufContainer ofWord(short i) {
        return new AbstractByteBufContainer.WordByteBufContainer(i);
    }

    static ByteBufContainer ofWord(int i) {
        return new AbstractByteBufContainer.WordByteBufContainer(i);
    }

    static ByteBufContainer ofDword(int i) {
        return new AbstractByteBufContainer.DWordByteBufContainer(i);
    }

    static ByteBufContainer ofDword(long i) {
        return new AbstractByteBufContainer.DWordByteBufContainer((int) i);
    }

    static ByteBufContainer ofBcd(String bcd) {
        return new AbstractByteBufContainer.BcdByteBufContainer(bcd);
    }

    static ByteBufContainer ofString(String string, Charset charset) {
        return new AbstractByteBufContainer.StringByteBufContainer(string, charset);
    }

    static ByteBufContainer ofString(String string) {
        return ofString(string, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    ByteBuf value();

    void release();

    @Override
    int length();

    default short byteValue() {
        return JtProtocolUtils.getUnsignedByte(this.value(), 0);
    }

    @Override
    default byte[] bytesValue() {
        return JtProtocolUtils.getBytes(this.value(), 0, this.length());
    }

    @Override
    default int wordValue() {
        return JtProtocolUtils.getWord(this.value(), 0);
    }

    @Override
    default long dwordValue() {
        return JtProtocolUtils.getUnsignedDword(this.value(), 0);
    }

    default String bcdValue() {
        return JtProtocolUtils.getBcd(this.value(), 0, this.length());
    }

    @Override
    default String stringValue(Charset charset) {
        return JtProtocolUtils.getString(this.value(), 0, this.length());
    }

}
