package io.github.hylexus.jt.utils;

import java.util.function.Predicate;

/**
 * @author hylexus
 */
public abstract class Assertions {
    private Assertions() {
    }

    public static <T> T check(T t, Predicate<T> predicate, String msg) {
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
}
