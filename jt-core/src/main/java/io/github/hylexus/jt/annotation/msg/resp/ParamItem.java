package io.github.hylexus.jt.annotation.msg.resp;

import io.github.hylexus.jt.data.MsgDataType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamItem {

    int order();

    MsgDataType dataType();

}