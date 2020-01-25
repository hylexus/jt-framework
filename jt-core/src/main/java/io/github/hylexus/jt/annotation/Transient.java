package io.github.hylexus.jt.annotation;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-08-28 12:16 上午
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transient {
}
