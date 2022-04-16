package io.github.hylexus.jt.utils;

import io.netty.buffer.ByteBuf;

public class HexStringUtils {

    private static final char[] DIGITS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & data[i]];
        }
        return out;
    }

    private static String encodeHex(ByteBuf byteBuf) {
        final StringBuilder builder = new StringBuilder();
        int readableBytes = byteBuf.readableBytes();
        for (int i = 0; i < readableBytes; i++) {
            final byte b = byteBuf.getByte(i);
            builder.append(DIGITS_HEX[(0xF0 & b) >>> 4]).append(DIGITS_HEX[0x0F & b]);
        }
        return builder.toString();
    }

    private static String encodeHexString(ByteBuf byteBuf, int start, int length) {
        final StringBuilder builder = new StringBuilder();

        for (int i = start; i < length; i++) {
            final byte b = byteBuf.getByte(i);
            builder.append(DIGITS_HEX[(0xF0 & b) >>> 4]).append(DIGITS_HEX[0x0F & b]);
        }
        return builder.toString();
    }

    private static byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 1) != 0) {
            throw new RuntimeException("字符个数应该为偶数");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static byte[] hexString2Bytes(String str) {
        return decodeHex(str.toCharArray());
    }

    public static String byteBufToString(ByteBuf byteBuf) {
        return encodeHex(byteBuf);
    }

    public static String byteBufToHexString(ByteBuf byteBuf) {
        return byteBufToHexString(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());
    }

    public static String byteBufToHexString(ByteBuf byteBuf, int start, int length) {
        return encodeHexString(byteBuf, start, length);
    }

    public static String bytes2HexString(byte[] bs) {
        return new String(encodeHex(bs));
    }

    public static String int2HexString(int n, int strLength) {
        return int2HexString(n, strLength, true);
    }

    public static String int2HexString(int n, int strLength, boolean withPrefix) {
        StringBuilder sb = new StringBuilder(Integer.toHexString(n));
        while (sb.length() < strLength) {
            sb.insert(0, "0");
        }
        if (withPrefix) {
            return "0x" + sb.toString();
        }
        return sb.toString();
    }
}