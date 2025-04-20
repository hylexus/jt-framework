package io.github.hylexus.jt.jt808.boot.config.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnJt808ServerCondition.class)
public @interface ConditionalOnJt808Server {

    Type type();

    enum Type {

        ANY,

        ALL,

        NONE,

        INSTRUCTION_SERVER,

        ATTACHMENT_SERVER,
    }

}
