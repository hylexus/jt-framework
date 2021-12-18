package io.github.hylexus.jt.jt808.support.utils;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.JT_808_STRING_ENCODING;
import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

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

    public static Object populateBasicField(byte[] bytes, Object instance, JavaBeanFieldMetadata fieldMetadata, MsgDataType type, int startIndex, int length)
            throws IllegalAccessException {
        final Object value;
        switch (type) {
            case WORD: {
                if (isShortType(fieldMetadata.getFieldType())) {
                    value = (short) intFromBytes(bytes, startIndex, length);
                } else {
                    value = intFromBytes(bytes, startIndex, length);
                }
                break;
            }
            case DWORD: {
                value = intFromBytes(bytes, startIndex, length);
                break;
            }
            case BYTE: {
                if (isByteType(fieldMetadata.getFieldType())) {
                    value = bytes[startIndex];
                } else if (isIntType(fieldMetadata.getFieldType())) {
                    value = (int) bytes[startIndex];
                } else {
                    // short
                    value = (short) bytes[startIndex];
                }
                break;
            }
            case BYTES: {
                value = Bytes.subSequence(bytes, startIndex, length);
                break;
            }
            case BCD: {
                value = BcdOps.bytes2BcdStringV2(bytes, startIndex, length);
                break;
            }
            case STRING: {
                value = new String(Bytes.subSequence(bytes, startIndex, length), JT_808_STRING_ENCODING);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        fieldMetadata.setFieldValue(instance, value);
        return value;
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
