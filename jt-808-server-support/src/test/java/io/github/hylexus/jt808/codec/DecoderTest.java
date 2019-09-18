package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.annotation.Jt808Field;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.msg.req.LocationUploadRequestMsg;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static io.github.hylexus.jt.config.JtProtocolConstant.JT_808_STRING_ENCODING;

/**
 * @author hylexus
 * Created At 2019-09-18 9:27 下午
 */
public class DecoderTest {

    private Map<Class<? extends DataTypeConverter>, DataTypeConverter> converterMapping = new HashMap<>();

    @Test
    public void test1() throws Exception {
        String hex = "0000000000000002026161D806EE1828000000000000140"
                + "80602400701040000000033182A4D30302C31352C31303430323130383736353433323123";
        byte[] bytes = HexStringUtils.hexString2Bytes(hex);

        Class<?> cls = LocationUploadRequestMsg.LocationUploadMsgBody.class;
        Object instance = cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            final Jt808Field annotation = field.getAnnotation(Jt808Field.class);
            if (annotation == null) {
                continue;
            }

            final MsgDataType dataType = annotation.dataType();
            final Class<?> fieldType = field.getType();
            final int startIndex = annotation.start();
            final int length = dataType.getByteCount() == 0
                    ? annotation.length()
                    : dataType.getByteCount();

            final Class<? extends DataTypeConverter> converterClass = annotation.customerDataTypeConverterClass();
            // 使用用户自定义的属性转换器
            if (converterClass != DataTypeConverter.NoOpsConverter.class) {
                populateByCustomerConverter(bytes, instance, field, converterClass, startIndex, length);
            } else {
                // 默认的实现转换策略
                if (dataType.getExpectedTargetClassType().contains(fieldType)) {
                    populate(bytes, instance, field, dataType, startIndex, length);
                } else {
                    // 没有配置【自定义属性转换器】&& 是不支持的目标类型
                    throw new IllegalArgumentException("No customerDataTypeConverterClass found, Unsupported expectedTargetClassType "
                            + fieldType + " for field " + field);
                }
            }

        }
        System.out.println(instance);
    }

    private void populate(byte[] bytes, Object instance, Field field, MsgDataType dataType, int startIndex, int length)
            throws IllegalAccessException {
        final Object value;
        switch (dataType) {
            case WORD:
            case DWORD: {
                value = IntBitOps.intFromBytes(bytes, startIndex, length);
                break;
            }
            case BYTE: {
                value = bytes[startIndex];
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

    private void populateByCustomerConverter(
            byte[] bytes, Object instance, Field field,
            Class<? extends DataTypeConverter> converterClass,
            int start, int byteCount) throws InstantiationException, IllegalAccessException {

        DataTypeConverter converter = getDataTypeConverter(converterClass);
        Object value = converter.convert(Bytes.subSequence(bytes, start, byteCount));
        setFieldValue(instance, field, value);
    }

    private DataTypeConverter getDataTypeConverter(
            Class<? extends DataTypeConverter> converterClass) throws InstantiationException, IllegalAccessException {

        DataTypeConverter converter = converterMapping.get(converterClass);
        if (converter == null) {
            converter = converterClass.newInstance();
            converterMapping.put(converterClass, converter);
        }
        return converter;
    }

    private void setFieldValue(Object instance, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }
}
