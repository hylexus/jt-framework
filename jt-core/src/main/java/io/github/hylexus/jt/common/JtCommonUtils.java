package io.github.hylexus.jt.common;

import io.github.hylexus.oaks.utils.BcdOps;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author hylexus
 */
@Slf4j
public abstract class JtCommonUtils {
    public static final Charset CHARSET_GBK = Charset.forName("GBK");

    public static short getUnsignedByte(ByteBuf byteBuf, int startIndex) {
        return byteBuf.getUnsignedByte(startIndex);
    }

    public static short readUnsignedByte(ByteBuf byteBuf) {
        return byteBuf.readUnsignedByte();
    }

    public static byte readByte(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    public static byte getByte(ByteBuf byteBuf, int startIndex) {
        return byteBuf.getByte(startIndex);
    }

    public static ByteBuf writeByte(ByteBuf byteBuf, byte value) {
        byteBuf.writeByte(value);
        return byteBuf;
    }

    public static ByteBuf writeByte(ByteBuf byteBuf, int value) {
        byteBuf.writeByte(value);
        return byteBuf;
    }

    public static ByteBuf writeByteBuf(ByteBuf to, ByteBuf from) {
        return writeByteBuf(to, from, true);
    }

    public static ByteBuf writeByteBuf(ByteBuf to, ByteBuf from, boolean autoClose) {
        try {
            to.writeBytes(from);
            return to;
        } finally {
            if (autoClose) {
                release(from);
            }
        }
    }

    public static int getWord(ByteBuf byteBuf, int start) {
        return byteBuf.getUnsignedShort(start);
    }

    public static int readUnsignedWord(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShort();
    }

    public static short readWord(ByteBuf byteBuf) {
        return byteBuf.readShort();
    }

    public static ByteBuf writeWord(ByteBuf byteBuf, int value) {
        return byteBuf.writeShort(value);
    }

    public static int getDword(ByteBuf byteBuf, int start) {
        return byteBuf.getInt(start);
    }

    public static int readDword(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    public static long readUnsignedDword(ByteBuf byteBuf) {
        return byteBuf.readUnsignedInt();
    }

    public static ByteBuf writeDword(ByteBuf byteBuf, int value) {
        return byteBuf.writeInt(value);
    }

    public static String getBcd(ByteBuf byteBuf, int startIndex, int length) {
        final byte[] bytes = getBytes(byteBuf, startIndex, length);
        return BcdOps.bcd2StringV2(bytes);
    }

    public static String readBcd(ByteBuf byteBuf, int length) {
        final byte[] bytes = readBytes(byteBuf, length);
        return BcdOps.bcd2StringV2(bytes);
    }

    public static ByteBuf writeBcd(ByteBuf byteBuf, String value) {
        final byte[] bytes = BcdOps.string2Bcd(value);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public static String getString(ByteBuf byteBuf, int startIndex, int length) {
        return getString(byteBuf, startIndex, length, CHARSET_GBK);
    }

    public static String getString(ByteBuf byteBuf, int startIndex, int length, Charset charset) {
        final byte[] bytes = getBytes(byteBuf, startIndex, length);
        return new String(bytes, charset);
    }

    public static String readString(ByteBuf byteBuf, int length) {
        return readString(byteBuf, length, CHARSET_GBK);
    }

    public static String readString(ByteBuf byteBuf, int length, Charset charset) {
        final byte[] bytes = readBytes(byteBuf, length);
        return new String(bytes, charset);
    }

    public static ByteBuf writeString(ByteBuf byteBuf, String value, Charset charset) {
        byteBuf.writeBytes(value.getBytes(charset));
        return byteBuf;
    }

    public static ByteBuf writeString(ByteBuf byteBuf, String value) {
        byteBuf.writeBytes(value.getBytes(CHARSET_GBK));
        return byteBuf;
    }

    public static byte[] getBytes(ByteBuf byteBuf) {
        return getBytes(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());
    }

    public static byte[] getBytes(ByteBuf byteBuf, int startIndex, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.getBytes(startIndex, bytes, 0, length);
        return bytes;
    }

    public static byte[] readBytes(ByteBuf byteBuf, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public static int setBitRange(int from, int length, int target, int offset) {
        return ((~(((1 << length) - 1) << offset)) & target)
                |
                (from << offset);
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

    public static void release(Object... objects) {
        if (objects == null || objects.length == 0) {
            return;
        }
        for (Object object : objects) {
            if (object == null) {
                continue;
            }
            try {
                if (object instanceof ReferenceCounted) {
                    if (((ReferenceCounted) object).refCnt() > 0) {
                        ((ReferenceCounted) object).release();
                    }
                }
            } catch (Throwable e) {
                log.error("", e);
            }
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
                // ignore
            }
        }
    }
}
