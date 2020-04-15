package io.github.hylexus.jt.codec.decode;

import io.github.hylexus.jt.annotation.msg.req.AdditionalField;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraField;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import io.github.hylexus.jt.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.jt.data.converter.registry.DataTypeConverterRegistry;
import io.github.hylexus.jt.data.converter.registry.DefaultDataTypeConverterRegistry;
import io.github.hylexus.jt.data.converter.req.entity.ReqMsgFieldConverter;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.exception.JtUnsupportedTypeException;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt.utils.ReflectionUtils;
import io.github.hylexus.oaks.utils.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author hylexus
 * Created At 2019-09-28 11:25 下午
 */
@Slf4j(topic = "jt-808.msg.req.decoder")
public class FieldDecoder {

    private final DataTypeConverterRegistry dataTypeConverterRegistry = new DefaultDataTypeConverterRegistry();

    private Map<Class<? extends ReqMsgFieldConverter>, ReqMsgFieldConverter> converterMapping = new HashMap<>();

    private final AdditionalFieldDecoder additionalFieldDecoder = new AdditionalFieldDecoder();
    private final ExtraFieldDecoder extraFieldDecoder = new ExtraFieldDecoder();
    private final SplittableFieldDecoder splittableFieldDecoder = new SplittableFieldDecoder();
    private final SlicedFromDecoder slicedFromDecoder = new SlicedFromDecoder();

    public <T> T decode(@NonNull Object instance, @NonNull byte[] bytes) throws IllegalAccessException, InstantiationException,
            InvocationTargetException {

        final Class<?> cls = instance.getClass();
        final JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);

        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {
            if (fieldMetadata.isAnnotationPresent(BasicField.class)) {
                processBasicField(cls, instance, fieldMetadata, bytes);
            } else if (fieldMetadata.isAnnotationPresent(ExtraField.class)) {
                processExtraField(cls, instance, fieldMetadata, bytes);
            } else if (fieldMetadata.isAnnotationPresent(AdditionalField.class)) {
                processAdditionalField(cls, instance, fieldMetadata, bytes);
            }
        }
        slicedFromDecoder.processAllSlicedFromField(instance);

        @SuppressWarnings("unchecked")
        T instance1 = (T) instance;
        return instance1;
    }

    private void processExtraField(Class<?> cls, @NonNull Object instance, JavaBeanFieldMetadata fieldMetadata, @NonNull byte[] bytes)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {

        ExtraField annotation = fieldMetadata.getAnnotation(ExtraField.class);
        int extraFieldLength = getExtraFieldLength(cls, instance, annotation);
        extraFieldDecoder.decodeExtraField(bytes, annotation.startIndex(), extraFieldLength, instance, fieldMetadata);
    }

    private void processAdditionalField(Class<?> cls, Object instance, JavaBeanFieldMetadata fieldMetadata, byte[] bytes)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

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
    }

    private void processBasicField(Class<?> cls, @NonNull Object instance, JavaBeanFieldMetadata fieldMetadata, @NonNull byte[] bytes)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Object value = processBasicFieldInternal(cls, instance, fieldMetadata, bytes);
        splittableFieldDecoder.processSplittableField(instance, fieldMetadata, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object processBasicFieldInternal(Class<?> cls, Object instance, JavaBeanFieldMetadata fieldMetadata, byte[] bytes)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        BasicField annotation = fieldMetadata.getAnnotation(BasicField.class);
        final MsgDataType dataType = annotation.dataType();
        final Class<?> fieldType = fieldMetadata.getFieldType();

        final int startIndex = getBasicFieldStartIndex(cls, instance, annotation);
        final int length = getBasicFieldLength(cls, instance, annotation, dataType);

        final Class<? extends ReqMsgFieldConverter> converterClass = annotation.customerDataTypeConverterClass();
        // 1. 优先使用用户自定义的属性转换器
        final Field field = fieldMetadata.getField();
        if (converterClass != ReqMsgFieldConverter.NoOpsConverter.class) {
            return populateFieldByCustomerConverter(bytes, instance, field, converterClass, startIndex, length);
        }

        // 2. 没有配置【自定义属性转换器】&& 是【不支持的目标类型】
        if (!dataType.getExpectedTargetClassType().contains(fieldType)) {
            throw new JtIllegalArgumentException("No customerDataTypeConverterClass found, Unsupported expectedTargetClassType "
                    + fieldType + " for field " + field);

        }
        // 3. 默认的属性转换策略
        // 3.1 LIST 特殊处理
        if (dataType == MsgDataType.LIST) {
            final ByteBuf buf = Unpooled.wrappedBuffer(Bytes.subSequence(bytes, startIndex, length));
            final List<Object> list = processListDataType(fieldMetadata, length, buf);
            fieldMetadata.setFieldValue(instance, list);
            return list;
        }

        // 3.2 其他类型
        final ConvertibleMetadata key = ConvertibleMetadata.forJt808MsgDataType(dataType, fieldType);
        final Optional<DataTypeConverter<?, ?>> converterInfo = dataTypeConverterRegistry.getConverter(key);
        if (!converterInfo.isPresent()) {
            // 3.2.1 没有配置【自定义属性转换器】&& 是【支持的目标类型】但是没有内置转换器
            throw new JtIllegalArgumentException("No customerDataTypeConverterClass found, Unsupported expectedTargetClassType "
                    + fieldType + " for field " + field);
        }

        final DataTypeConverter converter = converterInfo.get();
        final Object value;
        if (converter instanceof Jt808MsgDataTypeConverter) {
            value = ((Jt808MsgDataTypeConverter) converter).convert(bytes, startIndex, length);
        } else {
            log.warn("Converter missing match for type:{}", field);
            value = converter.convert(byte[].class, fieldType, Bytes.subSequence(bytes, startIndex, length));
        }
        log.debug("Convert field {}({}) by converter : {}, result : {}",
                field.getName(), fieldType.getSimpleName(),
                converter.getClass().getSimpleName(), value);
        fieldMetadata.setFieldValue(instance, value);
        return value;
    }

    private List<Object> processListDataType(JavaBeanFieldMetadata fieldMetadata, int itemTotalLength, ByteBuf buf)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        final Class<?> itemClass = fieldMetadata.getGenericType().get(0);
        final JavaBeanMetadata itemBeanMetadata = JavaBeanMetadataUtils.getBeanMetadata(itemClass);

        final List<Object> list = new ArrayList<>();

        List<JavaBeanFieldMetadata> fieldMetadataList = itemBeanMetadata.getFieldMetadataList();
        int totalProcessedBytesCount = 0;
        while (totalProcessedBytesCount < itemTotalLength && buf.isReadable()) {
            // process item of list
            final Object itemInstance = itemClass.newInstance();
            int processedBytesCount = 0;
            for (JavaBeanFieldMetadata itemFieldMetadata : fieldMetadataList) {
                final BasicField itemAnnotation = itemFieldMetadata.getAnnotation(BasicField.class);
                final int itemLength = getBasicFieldLength(itemClass, itemInstance, itemAnnotation, itemAnnotation.dataType());
                int retainedLength = itemLength + processedBytesCount;
                if (!buf.isReadable(retainedLength)) {
                    log.error("ba{}", buf);
                    break;
                }
                processedBytesCount += itemLength;
                byte[] itemBytes = ProtocolUtils.byteBufToByteArray(buf.retainedSlice(totalProcessedBytesCount, retainedLength), retainedLength);
                Object result = processBasicFieldInternal(itemClass, itemInstance, itemFieldMetadata, itemBytes);
                log.debug("Process LIST DataType for item {}, result:{}", itemInstance, result);
                splittableFieldDecoder.processSplittableField(itemInstance, itemFieldMetadata, result);
            }
            totalProcessedBytesCount += processedBytesCount;

            list.add(itemInstance);
        }
        return list;
    }

    private Object populateFieldByCustomerConverter(
            byte[] bytes, Object instance, Field field,
            Class<? extends ReqMsgFieldConverter> converterClass,
            int start, int byteCount) throws InstantiationException, IllegalAccessException {

        ReqMsgFieldConverter converter = getDataTypeConverter(converterClass);
        Object value = converter.convert(bytes, Bytes.subSequence(bytes, start, byteCount));
        ReflectionUtils.setFieldValue(instance, field, value);
        return value;
    }

    private ReqMsgFieldConverter getDataTypeConverter(
            Class<? extends ReqMsgFieldConverter> converterClass) throws InstantiationException, IllegalAccessException {

        ReqMsgFieldConverter converter = converterMapping.get(converterClass);
        if (converter == null) {
            synchronized (this) {
                converter = converterClass.newInstance();
                converterMapping.put(converterClass, converter);
            }
        }
        return converter;
    }

    private Integer getBasicFieldStartIndex(Class<?> cls, Object instance, BasicField annotation) throws InvocationTargetException, IllegalAccessException {
        if (StringUtil.isNullOrEmpty(annotation.startIndexMethod())) {
            return annotation.startIndex();
        }
        final Method method = getLengthMethod(cls, annotation.startIndexMethod());
        return getLengthFromByteCountMethod(instance, method);
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

    private int getExtraFieldLength(Class<?> cls, Object instance, ExtraField annotation)
            throws InvocationTargetException, IllegalAccessException {

        final int length = annotation.length();
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
