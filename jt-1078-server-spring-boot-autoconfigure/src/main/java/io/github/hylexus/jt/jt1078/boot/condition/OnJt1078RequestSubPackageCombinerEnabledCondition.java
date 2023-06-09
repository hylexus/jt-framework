package io.github.hylexus.jt.jt1078.boot.condition;

import io.github.hylexus.jt.jt1078.boot.props.subpackage.RequestSubPackageCombinerProps;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @author hylexus
 */
public class OnJt1078RequestSubPackageCombinerEnabledCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        final Map<String, Object> map = metadata.getAnnotationAttributes(ConditionalOnJt1078RequestSubPackageCombinerEnabled.class.getName());
        if (map == null || map.isEmpty()) {
            return false;
        }
        final RequestSubPackageCombinerProps storageProps = Binder.get(context.getEnvironment())
                .bind("jt1078.request-sub-package-combiner", RequestSubPackageCombinerProps.class)
                .orElse(new RequestSubPackageCombinerProps());

        final RequestSubPackageCombinerProps.Type type = (RequestSubPackageCombinerProps.Type) map.get("type");
        return storageProps.getType() == type;
    }
}
