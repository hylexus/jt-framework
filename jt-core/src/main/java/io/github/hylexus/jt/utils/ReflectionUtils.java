package io.github.hylexus.jt.utils;

import java.lang.reflect.Method;

/**
 * @author hylexus
 * Created At 2019-09-28 11:36 下午
 */
public class ReflectionUtils {

    // TODO 支持父类搜索
    public static <T> Method findMethod(Class<T> cls, String methodName) {
        try {
            return cls.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
