package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.annotation.DebugOnly;

/**
 * @author hylexus
 * Created At 2019-08-28 10:07 下午
 */
public class CommonUtils {

    public static boolean isBuiltinComponent(Class<?> userClass) {
        return userClass.isAnnotationPresent(BuiltinComponent.class)
               || io.github.hylexus.jt.common.BuiltinComponent.class.isAssignableFrom(userClass);
    }


    public static boolean isDeprecatedClass(Class<?> userClass) {
        return userClass.isAnnotationPresent(Deprecated.class)
               || userClass.isAnnotationPresent(DebugOnly.class)
               || io.github.hylexus.jt.common.DebugOnly.class.isAssignableFrom(userClass);
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
