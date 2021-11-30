package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.support.annotation.msg.basic.BasicField;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.Jt808HeaderSpecAware;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.converter.ReqMsgFieldConverter;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanMetadata;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.jt808.support.utils.ReflectionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class Jt808AnnotationBasedDecoder {

    private final Jt808FieldDeserializerRegistry jt808FieldDeserializerRegistry = new DefaultJt808FieldDeserializerRegistry();
    private final Map<Class<? extends ReqMsgFieldConverter<?>>, ReqMsgFieldConverter<?>> converterMapping = new HashMap<>();

    public <T> T decode(Jt808Request request, Class<T> cls) throws Jt808AnnotationArgumentResolveException {
        final T instance = ReflectionUtils.createInstance(cls);

        final Jt808ByteBuf bodyDataBuf = request.body();

        @SuppressWarnings("unchecked") final T result = (T) decode(cls, instance, bodyDataBuf, request);
        return result;
    }

    public Object decode(Class<?> cls, Object instance, ByteBuf byteBuf, Jt808Request request) throws Jt808AnnotationArgumentResolveException {
        this.processAwareMethod(cls, instance, request);
        final JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);

        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {
            if (fieldMetadata.isAnnotationPresent(BasicField.class)) {
                this.processBasicField(cls, instance, fieldMetadata, byteBuf, request);
                System.out.println("read--> " + byteBuf.readableBytes());
            }
        }
        return instance;
    }

    private Object processBasicField(Class<?> cls, Object instance, JavaBeanFieldMetadata fieldMetadata, ByteBuf bodyDataBuf, Jt808Request request) {
        final BasicField annotation = fieldMetadata.getAnnotation(BasicField.class);
        final MsgDataType dataType = annotation.dataType();
        final Class<?> fieldType = fieldMetadata.getFieldType();

        final int startIndex = getBasicFieldStartIndex(cls, instance, annotation);
        final int length = getBasicFieldLength(cls, instance, annotation, dataType);

        final Class<? extends ReqMsgFieldConverter<?>> converterClass = annotation.customerDataTypeConverterClass();
        // 1. 优先使用用户自定义的属性转换器
        final Field field = fieldMetadata.getField();
        if (converterClass != ReqMsgFieldConverter.NoOpsConverter.class) {
            return populateFieldByCustomerConverter(bodyDataBuf, instance, field, converterClass, startIndex, length);
        }

        // 2. 没有配置【自定义属性转换器】&& 是【不支持的目标类型】
        if (!dataType.getExpectedTargetClassType().contains(fieldType)) {
            throw new Jt808AnnotationArgumentResolveException("No customerDataTypeConverterClass found, Unsupported expectedTargetClassType "
                                                              + fieldType + " for field " + field);
        }
        // 3. 默认的属性转换策略
        // 3.1 LIST 特殊处理
        if (dataType == MsgDataType.LIST) {
            final List<Object> list = new ArrayList<>();
            final Class<?> itemClass = fieldMetadata.getGenericType().get(0);
            final ByteBuf slice = bodyDataBuf.slice(startIndex, length);
            while (slice.isReadable()) {
                final Object itemInstance = ReflectionUtils.createInstance(itemClass);
                final ByteBuf it = slice.slice(slice.readerIndex(), slice.readableBytes());
                this.decode(itemClass, itemInstance, it, request);
                slice.readerIndex(slice.readerIndex() + it.readerIndex());
                list.add(itemInstance);
            }
            this.setFieldValue(instance, fieldMetadata, list);
            return list;
        }

        // 3.2 其他类型
        final RequestMsgConvertibleMetadata key = ConvertibleMetadata.forJt808RequestMsgDataType(dataType, fieldType);
        final Optional<Jt808FieldDeserializer<?>> converterInfo = jt808FieldDeserializerRegistry.getConverter(key);
        if (converterInfo.isEmpty()) {
            // 3.2.1 没有配置【自定义属性转换器】&& 是【支持的目标类型】但是没有内置转换器
            throw new Jt808AnnotationArgumentResolveException(
                    "No customerDataTypeConverterClass found, Unsupported expectedTargetClassType " + fieldType + " for field " + field);
        }

        final Jt808FieldDeserializer<?> converter = converterInfo.get();
        final Object value = converter.deserialize(bodyDataBuf, dataType, startIndex, length);

        log.debug("Convert field {}({}) by converter : {}, result : {}",
                field.getName(), fieldType.getSimpleName(), converter.getClass().getSimpleName(), value);

        this.setFieldValue(instance, fieldMetadata, value);

        return value;
    }

    private Object populateFieldByCustomerConverter(
            ByteBuf bytes, Object instance, Field field,
            Class<? extends ReqMsgFieldConverter<?>> converterClass,
            int start, int byteCount) throws Jt808AnnotationArgumentResolveException {

        final ReqMsgFieldConverter<?> converter = getDataTypeConverter(converterClass);
        final Object value = converter.convert(bytes, start, byteCount);
        this.setFieldValue(instance, field, value);
        return value;
    }

    @SuppressWarnings("rawtypes")
    private ReqMsgFieldConverter getDataTypeConverter(
            Class<? extends ReqMsgFieldConverter<?>> converterClass) throws Jt808AnnotationArgumentResolveException {

        ReqMsgFieldConverter converter = converterMapping.get(converterClass);
        if (converter == null) {
            synchronized (this) {
                converter = ReflectionUtils.createInstance(converterClass);
                converterMapping.put(converterClass, converter);
            }
        }
        return converter;
    }

    private <T> void setFieldValue(T instance, JavaBeanFieldMetadata fieldMetadata, Object value) throws Jt808AnnotationArgumentResolveException {
        try {
            fieldMetadata.setFieldValue(instance, value);
        } catch (IllegalAccessException e) {
            throw new Jt808AnnotationArgumentResolveException(e);
        }
    }

    public void setFieldValue(Object instance, Field field, Object value) throws Jt808AnnotationArgumentResolveException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new Jt808AnnotationArgumentResolveException(e);
        }
    }

    private int getBasicFieldLength(Class<?> cls, Object instance, BasicField annotation, MsgDataType dataType)
            throws Jt808AnnotationArgumentResolveException {

        final int length = dataType.getByteCount() == 0
                ? annotation.length()
                : dataType.getByteCount();

        if (length > 0) {
            return length;
        }

        final Method lengthMethod = getLengthMethod(cls, annotation.byteCountMethod());

        return getLengthFromByteCountMethod(instance, lengthMethod);
    }

    private int getBasicFieldStartIndex(Class<?> cls, Object instance, BasicField annotation) throws Jt808AnnotationArgumentResolveException {
        if (StringUtil.isNullOrEmpty(annotation.startIndexMethod())) {
            return annotation.startIndex();
        }
        final Method method = getLengthMethod(cls, annotation.startIndexMethod());
        return getLengthFromByteCountMethod(instance, method);
    }


    private <T> int getLengthFromByteCountMethod(T instance, Method lengthMethod)
            throws Jt808AnnotationArgumentResolveException {

        final Object result;
        try {
            result = lengthMethod.invoke(instance);
        } catch (Exception e) {
            throw new Jt808AnnotationArgumentResolveException(e);
        }
        if (result instanceof Number) {
            return ((Number) result).intValue();
        }
        throw new Jt808AnnotationArgumentResolveException("byteCountMethod() return NaN");
    }

    private <T> Method getLengthMethod(Class<T> cls, String methodName) {
        final Method method = ReflectionUtils.findMethod(cls, methodName);
        if (method == null) {
            throw new NoSuchMethodError("No byteCountMethod() method found : " + methodName);
        }
        return method;
    }

    private <T> void processAwareMethod(Class<T> cls, Object instance, Jt808Request request) {
        if (instance instanceof Jt808HeaderSpecAware) {
            ((Jt808HeaderSpecAware) instance).setHeaderSpec(request.header());
        }
    }

}
