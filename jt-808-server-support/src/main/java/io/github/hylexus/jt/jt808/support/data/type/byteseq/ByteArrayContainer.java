package io.github.hylexus.jt.jt808.support.data.type.byteseq;

import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.support.data.type.BytesContainer;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.IntBitOps;

import java.nio.charset.Charset;

public interface ByteArrayContainer extends BytesContainer {

    static ByteArrayContainer ofByte(byte b) {
        return () -> new byte[]{b};
    }

    static ByteArrayContainer ofByte(short b) {
        return () -> new byte[]{(byte) b};
    }

    static ByteArrayContainer ofBytes(byte[] bytes) {
        return () -> bytes;
    }

    static ByteArrayContainer ofWord(short i) {
        return () -> IntBitOps.intTo2Bytes(i);
    }

    static ByteArrayContainer ofWord(int i) {
        return () -> IntBitOps.intTo2Bytes(i);
    }

    static ByteArrayContainer ofDword(int i) {
        return () -> IntBitOps.intTo4Bytes(i);
    }

    static ByteArrayContainer ofDword(long i) {
        return () -> IntBitOps.intTo4Bytes((int) i);
    }

    static ByteArrayContainer ofBcd(String bcd) {
        return () -> BcdOps.bcdString2bytes(bcd);
    }

    static ByteArrayContainer ofString(String string, Charset charset) {
        return () -> string.getBytes(charset);
    }

    static ByteArrayContainer ofString(String string) {
        return ofString(string, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    byte[] value();

    @Override
    default int length() {
        return this.value().length;
    }

    @Override
    default short byteValue() {
        return (short) (this.value()[0] & 0xFF);
    }

    @Override
    default byte[] bytesValue() {
        return this.value();
    }

    @Override
    default int wordValue() {
        return IntBitOps.intFrom2Bytes(this.value());
    }

    @Override
    default long dwordValue() {
        return IntBitOps.intFrom4Bytes(this.value()) & 0xFFFFFFFFL;
    }

    @Override
    default String bcdValue() {
        return BcdOps.bcd2StringV2(this.value());
    }

    @Override
    default String stringValue(Charset charset) {
        return new String(this.value(), charset);
    }
}
