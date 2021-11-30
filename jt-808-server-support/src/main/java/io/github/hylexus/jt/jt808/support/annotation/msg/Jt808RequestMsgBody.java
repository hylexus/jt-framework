package io.github.hylexus.jt.jt808.support.annotation.msg;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;

import java.lang.annotation.*;

/**
 * Created At 2019-09-18 8:47 下午
 *
 * @author hylexus
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestMsgBody {

    int[] msgType();

    String desc() default "";

    Jt808ProtocolVersion[] version() default {Jt808ProtocolVersion.AUTO_DETECTION};

}

