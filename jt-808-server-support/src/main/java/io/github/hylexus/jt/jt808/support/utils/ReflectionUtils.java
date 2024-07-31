package io.github.hylexus.jt.jt808.support.utils;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public class ReflectionUtils {

    public static <T> T createInstance(Class<T> cls) throws JtIllegalStateException {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new JtIllegalStateException(e);
        }
    }

    public static boolean isVoidReturnType(Method method) {
        return method.getReturnType() == Void.class || method.getReturnType() == void.class;
    }

    // TODO 支持父类搜索
    public static <T> Method findMethod(Class<T> cls, String methodName) {
        try {
            return cls.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static <T> Method searchMethod(Class<T> cls, String method) {
        return org.springframework.util.ReflectionUtils.findMethod(cls, method);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Object instance, Method method) {
        return (T) org.springframework.util.ReflectionUtils.invokeMethod(method, instance);
    }

    private static boolean isIntType(Class<?> cls) {
        return cls == Integer.class || cls == int.class;
    }

    private static boolean isShortType(Class<?> cls) {
        return cls == Short.class || cls == short.class;
    }

    private static boolean isByteType(Class<?> cls) {
        return cls == Byte.class || cls == byte.class;
    }

    @SuppressWarnings("deprecation")
    public static void setFieldValue(Object instance, Field field, Object value) {
        // if (!field.canAccess(instance)) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new JtIllegalStateException(e);
        }
    }

    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotation) {
        return AnnotatedElementUtils.getMergedAnnotation(annotatedElement, annotation);
    }

    public static <A extends Annotation> boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<A> annotation) {
        // TODO cache???
        return AnnotatedElementUtils.isAnnotated(annotatedElement, annotation);
    }

    @SafeVarargs
    public static <A extends Annotation> Map<Class<A>, Annotation> getAllAnnotations(Field field, Class<? extends Annotation>... extra) {
        final Map<Class<A>, Annotation> cache = new HashMap<>();
        for (Annotation annotation : field.getAnnotations()) {
            final Annotation mergedAnnotation = AnnotatedElementUtils.getMergedAnnotation(field, annotation.annotationType());
            if (mergedAnnotation != null) {
                @SuppressWarnings("unchecked") final Class<A> key = (Class<A>) annotation.annotationType();
                cache.put(key, mergedAnnotation);
            }
        }

        if (extra != null) {
            for (Class<? extends Annotation> annotationClass : extra) {
                final Annotation mergedAnnotation = AnnotatedElementUtils.getMergedAnnotation(field, annotationClass);
                if (mergedAnnotation != null) {
                    @SuppressWarnings("unchecked") final Class<A> key = (Class<A>) annotationClass;
                    cache.put(key, mergedAnnotation);
                }
            }
        }
        return cache;
    }
}
