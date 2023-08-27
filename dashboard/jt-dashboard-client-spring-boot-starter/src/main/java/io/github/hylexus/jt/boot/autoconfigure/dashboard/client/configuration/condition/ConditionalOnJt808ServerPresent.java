package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnConditionalJt808ServerPresentCondition.class)
public @interface ConditionalOnJt808ServerPresent {

}
