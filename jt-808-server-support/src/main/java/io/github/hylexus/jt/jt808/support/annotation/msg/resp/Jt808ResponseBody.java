package io.github.hylexus.jt.jt808.support.annotation.msg.resp;

import java.lang.annotation.*;

/**
 * Created At 2019-09-18 8:47 下午
 *
 * @author hylexus
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jt808ResponseBody {

    int respMsgId();

    String desc() default "";

    byte encryptionType() default 0;

    int maxPackageSize() default 1024;

    byte reversedBit15() default 0;
}
