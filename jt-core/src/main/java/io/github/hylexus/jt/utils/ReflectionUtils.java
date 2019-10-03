package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static io.github.hylexus.jt.config.JtProtocolConstant.JT_808_STRING_ENCODING;
import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

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

    public static void populateBasicField(byte[] bytes, Object instance, Field field, MsgDataType dataType, int startIndex, int length)
            throws IllegalAccessException {
        final Object value;
        switch (dataType) {
            case WORD: {
                value = (short) intFromBytes(bytes, startIndex, length);
                break;
            }
            case DWORD: {
                value = intFromBytes(bytes, startIndex, length);
                break;
            }
            case BYTE: {
                value = bytes[startIndex];
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
                throw new IllegalStateException("Unexpected value: " + dataType);
        }
        setFieldValue(instance, field, value);
    }

    public static void setFieldValue(Object instance, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }
}
