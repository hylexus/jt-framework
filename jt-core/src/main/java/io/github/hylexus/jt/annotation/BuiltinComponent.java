package io.github.hylexus.jt.annotation;

import io.github.hylexus.jt.core.OrderedComponent;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-08-28 12:16 上午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BuiltinComponent {

    int order() default OrderedComponent.LOWEST_PRECEDENCE;

}
