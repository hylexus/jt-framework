package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import static io.github.hylexus.jt.config.JtProtocolConstant.JT_808_STRING_ENCODING;
import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

/**
 * @author hylexus
 * Created At 2019-09-28 11:36 下午
 */
public class ReflectionUtils {

    public static Optional<Field> findFieldByName(Class<?> cls, String fieldName) {
        try {
            return Optional.of(cls.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            return Optional.empty();
        }
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
                value = BcdOps.bytes2BcdString(bytes, startIndex, length);
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

    public static void setFieldValue(Object instance, Field field, Object value) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(instance, value);
    }
}
