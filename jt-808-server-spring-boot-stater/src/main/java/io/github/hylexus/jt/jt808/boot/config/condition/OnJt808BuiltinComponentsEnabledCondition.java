package io.github.hylexus.jt.jt808.boot.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @author hylexus
 */
public class OnJt808BuiltinComponentsEnabledCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        final Environment environment = context.getEnvironment();
        final Map<String, Object> map = metadata.getAnnotationAttributes(ConditionalOnJt808BuiltinComponentsEnabled.class.getName());
        if (map == null || map.isEmpty()) {
            return false;
        }
        final BuiltinComponentType type = (BuiltinComponentType) map.get("value");
        switch (type) {
            case COMPONENT_STATISTICS: {
                final Boolean enabled = environment.getProperty("jt808.built-components.component-statistics.enabled", Boolean.class);
                return enabled == null || enabled;
            }
            case REQUEST_HANDLER: {
                final Boolean enabled = environment.getProperty("jt808.built-components.request-handlers.enabled", Boolean.class);
                return enabled == null || enabled;
            }
            default: {
                throw new IllegalArgumentException("Unknown componentType : " + type);
            }
        }
    }
}
