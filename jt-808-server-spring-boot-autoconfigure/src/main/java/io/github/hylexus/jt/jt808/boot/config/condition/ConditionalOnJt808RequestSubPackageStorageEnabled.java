package io.github.hylexus.jt.jt808.boot.config.condition;

import io.github.hylexus.jt.jt808.boot.props.builtin.RequestSubPackageStorageProps;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnJt808RequestSubPackageStorageEnabledCondition.class)
public @interface ConditionalOnJt808RequestSubPackageStorageEnabled {

    RequestSubPackageStorageProps.Type type();

}
