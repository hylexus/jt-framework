package io.github.hylexus.jt.annotation;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-09-18 8:47 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808MsgBody {

    int[] msgType();

}
