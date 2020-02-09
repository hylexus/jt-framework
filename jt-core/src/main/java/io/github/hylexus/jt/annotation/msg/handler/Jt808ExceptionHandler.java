package io.github.hylexus.jt.annotation.msg.handler;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2020-02-08 3:12 下午
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808ExceptionHandler {

    Class<? extends Throwable>[] value() default {};

}
