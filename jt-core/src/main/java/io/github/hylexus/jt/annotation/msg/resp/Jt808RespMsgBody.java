package io.github.hylexus.jt.annotation.msg.resp;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RespMsgBody {

    int[] msgType();

}