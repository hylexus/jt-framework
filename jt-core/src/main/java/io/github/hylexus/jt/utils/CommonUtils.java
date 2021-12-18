package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.annotation.DebugOnly;

import java.lang.reflect.Method;

/**
 * @author hylexus
 */
public class CommonUtils {

    public static boolean isBuiltinComponent(Class<?> userClass) {
        return userClass.isAnnotationPresent(BuiltinComponent.class)
               || io.github.hylexus.jt.core.BuiltinComponent.class.isAssignableFrom(userClass);
    }

    public static boolean isBuiltinComponent(Method method) {
        return method.isAnnotationPresent(BuiltinComponent.class)
               || isBuiltinComponent(method.getDeclaringClass());
    }

    public static boolean isDeprecatedComponent(Class<?> userClass) {
        return userClass.isAnnotationPresent(Deprecated.class)
               || userClass.isAnnotationPresent(DebugOnly.class)
               || DebugOnly.class.isAssignableFrom(userClass);
    }

    public static boolean isDeprecatedComponent(Method method) {
        return method.isAnnotationPresent(Deprecated.class)
               || method.isAnnotationPresent(DebugOnly.class)
               || isDeprecatedComponent(method.getDeclaringClass());
    }

    public static String shortClassName(Class<?> cls) {
        String[] arr = cls.getName().split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            stringBuilder.append(arr[i].charAt(0)).append(".");
        }
        return stringBuilder.append(arr[arr.length - 1]).toString();
    }
}
