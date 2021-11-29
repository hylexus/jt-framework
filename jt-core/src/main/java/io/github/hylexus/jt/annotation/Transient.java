package io.github.hylexus.jt.annotation;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transient {
}
