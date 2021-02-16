package io.github.hylexus.jt.utils;

/**
 * Created At 2021/02/15 20:51
 *
 * @author hylexus
 */
public class Assertions {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

}
