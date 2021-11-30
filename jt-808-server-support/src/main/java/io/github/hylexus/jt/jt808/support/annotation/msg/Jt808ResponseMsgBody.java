package io.github.hylexus.jt.jt808.support.annotation.msg;

import java.lang.annotation.*;

/**
 * Created At 2019-09-18 8:47 下午
 *
 * @author hylexus
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808ResponseMsgBody {

    int respMsgId();

    String desc() default "";

}
