package io.github.hylexus.jt.jt808.support.utils;

import io.github.hylexus.jt.exception.JtIllegalStateException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    private static boolean isIntType(Class<?> cls) {
        return cls == Integer.class || cls == int.class;
    }

    private static boolean isShortType(Class<?> cls) {
        return cls == Short.class || cls == short.class;
    }

    private static boolean isByteType(Class<?> cls) {
        return cls == Byte.class || cls == byte.class;
    }

    public static void setFieldValue(Object instance, Field field, Object value) {
        // if (!field.isAccessible()) {
        if (!field.canAccess(instance)) {
            field.setAccessible(true);
        }
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new JtIllegalStateException(e);
        }
    }
}
