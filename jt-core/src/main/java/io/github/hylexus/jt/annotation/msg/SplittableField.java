package io.github.hylexus.jt.annotation.msg;

import com.google.common.collect.Sets;

import java.lang.annotation.*;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-03 8:55 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SplittableField {

    Set<Class<?>> set = Sets.newHashSet(int.class, Integer.class);

    String splitPropsIntoField();

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface BitAt {
        int value();
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface BitAtRange {

        int startIndex();

        int endIndex();
    }
}
