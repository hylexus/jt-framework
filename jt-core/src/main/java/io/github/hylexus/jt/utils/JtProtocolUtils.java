package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.oaks.utils.BcdOps;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @author hylexus
 */
public abstract class JtProtocolUtils {
    public static String getBcd(ByteBuf byteBuf, int startIndex, int length) {
        final byte[] bytes = getBytes(byteBuf, startIndex, length);
        return BcdOps.bcd2StringV2(bytes);
    }

    public static String readBcd(ByteBuf byteBuf, int startIndex, int length) {
        final byte[] bytes = readBytes(byteBuf, startIndex, length);
        return BcdOps.bcd2StringV2(bytes);
    }

    public static String getString(ByteBuf byteBuf, int startIndex, int length) {
        return getString(byteBuf, startIndex, length, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public static String getString(ByteBuf byteBuf, int startIndex, int length, Charset charset) {
        final byte[] bytes = getBytes(byteBuf, startIndex, length);
        return new String(bytes, charset);
    }

    public static String readString(ByteBuf byteBuf, int startIndex, int length) {
        return readString(byteBuf, startIndex, length, JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public static String readString(ByteBuf byteBuf, int startIndex, int length, Charset charset) {
        final byte[] bytes = readBytes(byteBuf, startIndex, length);
        return new String(bytes, charset);
    }

    public static byte[] getBytes(ByteBuf byteBuf, int startIndex, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.getBytes(startIndex, bytes, 0, length);
        return bytes;
    }

    public static byte[] readBytes(ByteBuf byteBuf, int startIndex, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public static int generateMsgBodyPropsForJt808(int msgBodySize, int encryptionType, boolean isSubPackage, Jt808ProtocolVersion version, int reversedBit15) {
        // [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
        int props = (msgBodySize & 0x3FF)
                    // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
                    | ((encryptionType << 10) & 0x1C00)
                    // [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
                    | (((isSubPackage ? 1 : 0) << 13) & 0x2000)
                    // [14_ ]  0100,0000,0000,0000(4000)(保留位)
                    | ((version.getVersionBit() << 14) & 0x4000)
                    // [15_ ]  1000,0000,0000,0000(8000)(保留位)
                    | ((reversedBit15 << 15) & 0x8000);
        return props & 0xFFFF;
    }

    public static int setBitRange(int value, int startIndex, int targetValue, int targetLength) {
        int mask = 32 - targetLength;
        return targetValue << mask >>> mask << startIndex | value;
    }

    public static String toBinaryString(int value, int width) {
        final String binaryString = Integer.toBinaryString(value);
        if (binaryString.length() >= width) {
            return binaryString;
        }
        StringBuilder sb = new StringBuilder(binaryString);
        while (sb.length() < width) {
            sb.insert(0, 0);
        }
        return sb.toString();
    }
}
