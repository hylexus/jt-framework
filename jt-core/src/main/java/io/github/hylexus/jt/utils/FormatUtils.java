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
        return Jdk8Adapter.stringRepeat("0", prefixLen) + binaryString;
    }

    public static String toHexString(int i) {
        return Integer.toHexString(i);
    }

    public static String toHexString(int i, int minLen) {
        final String hexString = Integer.toHexString(i);
        final int prefixLen = Math.max(minLen - hexString.length(), 0);
        return Jdk8Adapter.stringRepeat("0", prefixLen) + hexString;
    }

    public static String toHexString(ByteBuf byteBuf) {
        return HexStringUtils.byteBufToHexString(byteBuf);
    }

    public static String toHexString(byte[] bytes) {
        return HexStringUtils.bytes2HexString(bytes);
    }
}
