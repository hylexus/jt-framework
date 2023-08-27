package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.condition;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

/**
 * @author hylexus
 */
public class OnConditionalJt1078ServerPresentCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {

        final JtApplicationProps props = Binder.get(context.getEnvironment())
                .bind("jt-dashboard.client", JtApplicationProps.class)
                .orElse(new JtApplicationProps());
        if (!props.getJt1078().isEnabled()) {
            return false;
        }
        return ClassUtils.isPresent("io.github.hylexus.jt.jt1078.spec.Jt1078Request", OnConditionalJt1078ServerPresentCondition.class.getClassLoader());
    }
}
