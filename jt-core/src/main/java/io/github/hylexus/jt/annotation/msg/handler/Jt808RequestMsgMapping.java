package io.github.hylexus.jt.annotation.msg.handler;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2020-02-01 3:25 下午
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestMsgMapping {

    int[] msgType();

    String desc() default "";

}
