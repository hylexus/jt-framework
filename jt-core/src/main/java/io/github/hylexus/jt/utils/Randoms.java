package io.github.hylexus.jt.utils;

import java.util.concurrent.ThreadLocalRandom;

public class Randoms {
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static String randomString(int length) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET[random.nextInt(ALPHABET.length)]);
        }
        return sb.toString();
    }

    public static byte[] randomBytes(int length) {
        final byte[] bytes = new byte[length];
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }
}
