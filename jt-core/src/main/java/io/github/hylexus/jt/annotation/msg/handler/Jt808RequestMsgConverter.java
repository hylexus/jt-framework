package io.github.hylexus.jt.annotation.msg.handler;

import java.lang.annotation.*;

/**
 * @author lirenhao
 * date: 2020/9/10 4:20 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RequestMsgConverter {

    int msgType();

    String desc() default "";
}
