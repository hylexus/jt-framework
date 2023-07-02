package io.github.hylexus.jt.utils;

import io.netty.buffer.ByteBuf;

public final class FormatUtils {
    private FormatUtils() {
    }

    public static String toBinaryString(int i) {
        return Integer.toBinaryString(i);
    }

    public static String toBinaryString(int i, int minLen) {
        final String binaryString = Integer.toBinaryString(i);
        final int prefixLen = Math.max(minLen - binaryString.length(), 0);
        return "0".repeat(prefixLen) + binaryString;
    }

    public static String toHexString(ByteBuf byteBuf) {
        return HexStringUtils.byteBufToHexString(byteBuf);
    }
}
