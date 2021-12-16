package io.github.hylexus.jt.jt808.boot.config.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(prefix = "jt808.built-components.request-handlers", name = "enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnJt808BuiltinComponentsEnabled {
}
