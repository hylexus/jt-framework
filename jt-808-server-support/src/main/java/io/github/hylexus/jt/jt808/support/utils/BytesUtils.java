package io.github.hylexus.jt.jt808.support.utils;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.oaks.utils.BcdOps;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @author hylexus
 */
public abstract class BytesUtils {

    public static int getWord(ByteBuf byteBuf, int startIndex) {
        return byteBuf.getUnsignedShort(startIndex);
    }

    public static int readWord(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShort();
    }

    public static ByteBuf writeWord(ByteBuf byteBuf, int value) {
        byteBuf.writeShort(value);
        return byteBuf;
    }

    public static int getDword(ByteBuf byteBuf, int startIndex) {
        return byteBuf.getInt(startIndex);
    }

    public static int readDword(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    public static ByteBuf writeDword(ByteBuf byteBuf, int value) {
        byteBuf.writeInt(value);
        return byteBuf;
    }

    public static String getString(ByteBuf byteBuf, int offset, int length) {
        return getString(byteBuf, offset, length, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public static String getString(ByteBuf byteBuf, int offset, int length, Charset charset) {
        final byte[] bytes = getBytes(byteBuf, offset, length);
        return new String(bytes, charset);
    }

    public static String readString(ByteBuf byteBuf, int offset, int length) {
        return readString(byteBuf, offset, length, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public static String readString(ByteBuf byteBuf, int offset, int length, Charset charset) {
        final byte[] bytes = readBytes(byteBuf, length);
        return new String(bytes, charset);
    }


    public static ByteBuf writeString(ByteBuf byteBuf, String value, Charset charset) {
        byteBuf.writeBytes(value.getBytes(charset));
        return byteBuf;
    }

    public static ByteBuf writeString(ByteBuf byteBuf, String value) {
        return writeString(byteBuf, value, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public static String getBcd(ByteBuf byteBuf, int offset, int length) {
        final byte[] bytes = getBytes(byteBuf, offset, length);
        return BcdOps.bcd2StringV2(bytes);
    }

    public static String readBcd(ByteBuf byteBuf, int length) {
        final byte[] bytes = readBytes(byteBuf, length);
        return BcdOps.bcd2StringV2(bytes);
    }

    public static void writeBcd(ByteBuf byteBuf, String value) {
        final byte[] bytes = BcdOps.string2Bcd(value);
        byteBuf.writeBytes(bytes);
    }

    public static byte[] getBytes(ByteBuf byteBuf, int offset, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.getBytes(offset, bytes, 0, length);
        return bytes;
    }


    public static byte[] readBytes(ByteBuf byteBuf, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
