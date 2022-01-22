package io.github.hylexus.jt.annotation;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DebugOnly {
}
