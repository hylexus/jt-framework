package io.github.hylexus.jt.annotation.msg.req.slice;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-10-03 8:55 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SplittableField {

    /**
     * @return 字段名(将属性拆分到嵌套的bean中)
     */
    String splitPropertyValueIntoNestedBeanField();

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface BitAt {
        int bitIndex();
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface BitAtRange {

        int startIndex();

        int endIndex();
    }
}
