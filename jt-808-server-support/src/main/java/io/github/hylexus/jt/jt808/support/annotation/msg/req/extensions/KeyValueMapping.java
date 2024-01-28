package io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions;

public @interface KeyValueMapping {

    int key();

    ValueDescriptor value();

    String desc() default "";
}
