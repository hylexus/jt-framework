package io.github.hylexus.jt.annotation.msg.resp;

import io.github.hylexus.jt.data.MsgDataType;

import java.lang.annotation.*;

import static io.github.hylexus.jt.data.MsgDataType.UNKNOWN;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandField {

    int order();

    MsgDataType dataType() default UNKNOWN;

}