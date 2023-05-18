package io.github.hylexus.jt.jt808.support.annotation.msg;

public @interface Padding {
    /**
     * 默认为 {@code 0}.
     */
    // 48 --> "0".getBytes()[0]
    byte paddingElement() default 48;

    int minLength();
}