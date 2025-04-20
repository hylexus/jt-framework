package io.github.hylexus.jt.jt808.boot.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;

class OnJt808ServerCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        final Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnJt808Server.class.getName());
        if (annotationAttributes == null || annotationAttributes.isEmpty()) {
            return false;
        }

        final ConditionalOnJt808Server.Type type = (ConditionalOnJt808Server.Type) annotationAttributes.get("type");
        final Environment environment = context.getEnvironment();
        switch (type) {
            case INSTRUCTION_SERVER:
                return this.instructionServerEnabled(environment);
            case ATTACHMENT_SERVER:
                return this.attachmentServerEnabled(environment);
            case ANY:
                return this.instructionServerEnabled(environment) || this.attachmentServerEnabled(environment);
            case ALL:
                return this.instructionServerEnabled(environment) && this.attachmentServerEnabled(environment);
            case NONE:
                return !this.instructionServerEnabled(environment) && !this.attachmentServerEnabled(environment);
            default:
                // 不应该执行到这里
                throw new IllegalStateException();
        }
    }

    private boolean instructionServerEnabled(Environment environment) {
        return environment.getProperty("jt808.server.enabled", Boolean.class, true);
    }

    private boolean attachmentServerEnabled(Environment environment) {
        return environment.getProperty("jt808.attachment-server.enabled", Boolean.class, false);
    }

}
