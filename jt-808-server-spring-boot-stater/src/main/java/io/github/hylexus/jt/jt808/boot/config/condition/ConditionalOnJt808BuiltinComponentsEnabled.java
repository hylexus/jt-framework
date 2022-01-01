package io.github.hylexus.jt.jt808.boot.config.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnJt808BuiltinComponentsEnabledCondition.class)
public @interface ConditionalOnJt808BuiltinComponentsEnabled {

    BuiltinComponentType value();

}
