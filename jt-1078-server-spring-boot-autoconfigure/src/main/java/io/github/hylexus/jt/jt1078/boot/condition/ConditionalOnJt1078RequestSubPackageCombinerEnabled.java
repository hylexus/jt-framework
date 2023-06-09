package io.github.hylexus.jt.jt1078.boot.condition;

import io.github.hylexus.jt.jt1078.boot.props.subpackage.RequestSubPackageCombinerProps;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnJt1078RequestSubPackageCombinerEnabledCondition.class)
public @interface ConditionalOnJt1078RequestSubPackageCombinerEnabled {

    RequestSubPackageCombinerProps.Type type();

}
