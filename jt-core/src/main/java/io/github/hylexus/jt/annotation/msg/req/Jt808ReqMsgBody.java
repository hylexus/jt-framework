package io.github.hylexus.jt.annotation.msg.req;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-09-18 8:47 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808ReqMsgBody {

    int[] msgType();

    String desc() default "";

    Jt808ProtocolVersion[] version() default {Jt808ProtocolVersion.AUTO_DETECTION};

}

