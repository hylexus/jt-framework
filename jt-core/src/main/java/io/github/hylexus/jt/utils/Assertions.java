package io.github.hylexus.jt.utils;

import java.util.function.Predicate;

/**
 * @author hylexus
 */
public abstract class Assertions {
    private Assertions() {
    }

    public static void assertThat(boolean condition, String msg) {
        if (!condition) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T> T assertThat(T t, Predicate<T> predicate, String msg) {
        if (!predicate.test(t)) {
            throw new IllegalArgumentException(msg);
        }
        return t;
    }

    public static <T> T requireNonNull(T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
        return obj;
    }

    public static int range(int n, int left, int right, String msg) {
        if (n < left || n >= right) {
            throw new IllegalArgumentException(msg);
        }
        return n;
    }

    public static int rangeClosed(int n, int left, int right, String msg) {
        return range(n, left, right + 1, msg);
    }
}
