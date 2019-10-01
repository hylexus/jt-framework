package io.github.hylexus.jt.codec;

import io.github.hylexus.jt.annotation.msg.AdditionalField;
import io.github.hylexus.jt.annotation.msg.BasicField;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import io.github.hylexus.jt.exception.JtUnsupportedTypeException;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.utils.ReflectionUtils;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static io.github.hylexus.jt.config.JtProtocolConstant.JT_808_STRING_ENCODING;
import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

/**
 * @author hylexus
 * Created At 2019-09-28 11:25 下午
 */
public class FieldDecoder {

    private Map<Class<? extends DataTypeConverter>, DataTypeConverter> converterMapping = new HashMap<>();

    private AdditionalFieldDecoder additionalFieldDecoder = new AdditionalFieldDecoder();

    public <T> T decode(@NonNull Object instance, @NonNull byte[] bytes) throws IllegalAccessException, InstantiationException,
            InvocationTargetException {

        final Class<?> cls = instance.getClass();
        final JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);

        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {

            if (fieldMetadata.isAnnotationPresent(BasicField.class)) {
                processBasicField(cls, bytes, instance, fieldMetadata);
                // 目前为止BasicField不支持和其他类型注解一起使用, continue直接处理下一个Field
                continue;
            }

            if (fieldMetadata.isAnnotationPresent(AdditionalField.class)) {
                processAdditionalField(instance, bytes, cls, fieldMetadata);
            }

        }

        @SuppressWarnings("unchecked")
        T instance1 = (T) instance;
        return instance1;
    }

    private int processAdditionalField(Object instance, byte[] bytes, Class<?> cls, JavaBeanFieldMetadata fieldMetadata)
            throws IllegalAccessException, InvocationTargetException {

        AdditionalField annotation = fieldMetadata.getAnnotation(AdditionalField.class);
        if (!AdditionalField.SUPPORTED_TARGET_CLASS.contains(fieldMetadata.getFieldType())) {
            throw new JtUnsupportedTypeException("Unsupported type ["
                    + fieldMetadata.getFieldType() + "] found in a field marked by " + AdditionalField.class.getSimpleName());
        }

        // 附加项起始位置
        int startIndex = annotation.startIndex();
        // 附加项总长度
        int totalLength = getAdditionalFieldLength(cls, instance, annotation);
        this.additionalFieldDecoder.decodeAdditionalField(instance, bytes, startIndex, totalLength, fieldMetadata);
        return totalLength;
    }

    private int processBasicField(Class<?> cls, byte[] bytes, Object instance, JavaBeanFieldMetadata fieldMetadata)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        BasicField annotation = fieldMetadata.getAnnotation(BasicField.class);
        final MsgDataType dataType = annotation.dataType();
        final Class<?> fieldType = fieldMetadata.getFieldType();
        final int startIndex = annotation.startIndex();

        int length = getBasicFieldLength(cls, instance, annotation, dataType);

        final Class<? extends DataTypeConverter> converterClass = annotation.customerDataTypeConverterClass();
        // 1. 优先使用用户自定义的属性转换器
        final Field field = fieldMetadata.getField();
        if (converterClass != DataTypeConverter.NoOpsConverter.class) {
            populateFieldByCustomerConverter(bytes, instance, field, converterClass, startIndex, length);
            return length;
        }

        // 2. 默认的属性转换策略
        if (dataType.getExpectedTargetClassType().contains(fieldType)) {
            populateBasicField(bytes, instance, field, dataType, startIndex, length);
            return length;
        }

        // 3. 没有配置【自定义属性转换器】&& 是【不支持的目标类型】
        throw new IllegalArgumentException("No customerDataTypeConverterClass found, Unsupported expectedTargetClassType "
                + fieldType + " for field " + field);
    }

    private void populateBasicField(byte[] bytes, Object instance, Field field, MsgDataType dataType, int startIndex, int length)
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

    private void populateFieldByCustomerConverter(
            byte[] bytes, Object instance, Field field,
            Class<? extends DataTypeConverter> converterClass,
            int start, int byteCount) throws InstantiationException, IllegalAccessException {

        DataTypeConverter converter = getDataTypeConverter(converterClass);
        Object value = converter.convert(bytes, Bytes.subSequence(bytes, start, byteCount));
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

    private Integer getBasicFieldLength(Class<?> cls, Object instance, BasicField annotation, MsgDataType dataType)
            throws IllegalAccessException, InvocationTargetException {

        final int length = dataType.getByteCount() == 0
                ? annotation.length()
                : dataType.getByteCount();

        if (length > 0) {
            return length;
        }

        final Method lengthMethod = getLengthMethod(cls, annotation.byteCountMethod());

        return getLengthFromByteCountMethod(instance, lengthMethod);
    }

    private int getAdditionalFieldLength(Class<?> cls, Object instance, AdditionalField annotation)
            throws IllegalAccessException, InvocationTargetException {

        final int length = annotation.length();
        if (length > 0) {
            return length;
        }

        final Method lengthMethod = getLengthMethod(cls, annotation.byteCountMethod());

        return getLengthFromByteCountMethod(instance, lengthMethod);
    }

    private <T> Integer getLengthFromByteCountMethod(T instance, Method lengthMethod)
            throws IllegalAccessException, InvocationTargetException {

        return (Integer) lengthMethod.invoke(instance);
    }

    private <T> Method getLengthMethod(Class<T> cls, String methodName) {
        Method method = ReflectionUtils.findMethod(cls, methodName);
        if (method == null) {
            throw new NoSuchMethodError("No byteCountMethod() method found : " + methodName);
        }
        return method;
    }

}
