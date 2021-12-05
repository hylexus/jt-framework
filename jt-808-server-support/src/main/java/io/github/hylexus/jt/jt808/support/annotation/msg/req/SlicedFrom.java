package io.github.hylexus.jt.jt808.support.annotation.msg.req;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SlicedFrom {

    int DEFAULT_BIT_INDEX = -1;

    String sourceFieldName();

    int bitIndex() default DEFAULT_BIT_INDEX;

    int startBitIndex() default DEFAULT_BIT_INDEX;

    int endBitIndex() default DEFAULT_BIT_INDEX;
}
