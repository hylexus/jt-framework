package io.github.hylexus.jt.jt808.boot.config.condition;

import io.github.hylexus.jt.jt808.boot.props.builtin.RequestSubPackageStorageProps;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @author hylexus
 */
public class OnJt808RequestSubPackageStorageEnabledCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        final Map<String, Object> map = metadata.getAnnotationAttributes(ConditionalOnJt808RequestSubPackageStorageEnabled.class.getName());
        if (map == null || map.isEmpty()) {
            return false;
        }
        final RequestSubPackageStorageProps storageProps = Binder.get(context.getEnvironment())
                .bind("jt808.request-sub-package-storage", RequestSubPackageStorageProps.class)
                .orElse(new RequestSubPackageStorageProps());

        final RequestSubPackageStorageProps.Type type = (RequestSubPackageStorageProps.Type) map.get("type");
        return storageProps.getType() == type;
    }
}
