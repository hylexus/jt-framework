package io.github.hylexus.jt.jt808.support.annotation.handler;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808ExceptionHandler {

    Class<? extends Throwable>[] value();

}
