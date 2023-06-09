package io.github.hylexus.jt.utils;

import io.netty.buffer.ByteBuf;

public final class ByteBufUtils {
    private ByteBufUtils() {
    }

    public static byte[] getBytes(ByteBuf byteBuf) {
        final int length = byteBuf.readableBytes();
        return getBytes(byteBuf, 0, length);
    }

    public static byte[] getBytes(ByteBuf byteBuf, int startIndex, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.getBytes(startIndex, bytes, 0, length);
        return bytes;
    }
}
