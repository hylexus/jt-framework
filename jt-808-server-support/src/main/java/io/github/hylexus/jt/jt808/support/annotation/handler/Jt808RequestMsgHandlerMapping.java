package io.github.hylexus.jt.jt808.support.annotation.handler;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestMsgHandlerMapping {

    int[] msgType();

    String desc() default "";

    Jt808ProtocolVersion[] versions() default {Jt808ProtocolVersion.AUTO_DETECTION};

}
