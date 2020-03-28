package io.github.hylexus.jt.annotation.msg.resp;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-09-18 8:47 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808RespMsgBody {

    int respMsgId();

    String desc() default "";

}
