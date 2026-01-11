package io.github.hylexus.jt.utils;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@NullMarked
public final class JtAnnotationUtils {
    private JtAnnotationUtils() {
        throw new UnsupportedOperationException();
    }

    public static <A extends Annotation> @Nullable A getMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return AnnotatedElementUtils.getMergedAnnotation(element, annotationType);
    }

}
