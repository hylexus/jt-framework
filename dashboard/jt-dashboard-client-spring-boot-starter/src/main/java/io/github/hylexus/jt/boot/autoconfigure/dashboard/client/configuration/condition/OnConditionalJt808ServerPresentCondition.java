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
public class OnConditionalJt808ServerPresentCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {

        final JtApplicationProps props = Binder.get(context.getEnvironment())
                .bind("jt-dashboard.client", JtApplicationProps.class)
                .orElse(new JtApplicationProps());
        if (!props.getJt808().isEnabled()) {
            return false;
        }
        return ClassUtils.isPresent("io.github.hylexus.jt.jt808.spec.Jt808Request", OnConditionalJt808ServerPresentCondition.class.getClassLoader());
    }
}
