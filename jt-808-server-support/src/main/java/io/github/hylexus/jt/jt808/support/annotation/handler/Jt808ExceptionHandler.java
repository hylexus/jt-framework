package io.github.hylexus.jt.jt808.support.annotation.handler;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808ExceptionHandler {

    Class<? extends Throwable>[] value() default {};

    Jt808ProtocolVersion[] versions() default {Jt808ProtocolVersion.AUTO_DETECTION};

}
