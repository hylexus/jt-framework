package io.github.hylexus.jt.data.converter.impl;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import io.github.hylexus.jt.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.jt.data.converter.registry.DataTypeConverterRegistry;
import io.github.hylexus.jt.data.converter.registry.DefaultDataTypeConverterRegistry;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created At 2020-08-15 12:43
 *
 * @author hylexus
 */
public class ByteArrayToListDataTypeConverter implements Jt808MsgDataTypeConverter<List<Object>> {
    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET;
    private final DelegateDataTypeConverter delegateDataTypeConverter = new DelegateDataTypeConverter();

    static {
        Set<ConvertibleMetadata> set = new HashSet<>();
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.LIST, List.class));
        CONVERTIBLE_METADATA_SET = Collections.unmodifiableSet(set);
    }

    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    /**
     * 实现有点恶心了，代码已经很乱了……
     * 先这样吧  后面再重构
     */
    @Override
    public List<Object> convert(@Nullable ConvertibleMetadata key, JavaBeanFieldMetadata targetFieldMetadata, byte[] sourceInstance,
                                @Nullable List<Object> targetInstance, @Nullable MsgDataType itemMsgDataType) {
        if (targetInstance == null) {
            targetInstance = new ArrayList<>();
        }
        Object result = convertByDelegate(sourceInstance, targetFieldMetadata, itemMsgDataType);
        targetInstance.add(result);
        return targetInstance;
    }

    @Override
    public List<Object> convert(byte[] bytes, int start, int length) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings(value = {"unchecked", "rawtype"})
    private Object convertByDelegate(byte[] sourceInstance, JavaBeanFieldMetadata targetFieldMetadata, MsgDataType itemMsgDataType) {

        // 获取List元素泛型
        Class itemClass = getListItemGenericClass(targetFieldMetadata);
        if (itemClass == null) {
            return null;
        }

        ConvertibleMetadata key = ConvertibleMetadata.forJt808MsgDataType(itemMsgDataType, itemClass);
        Object converterInfo = delegateDataTypeConverter.getConverter(key);

        if (!((Optional<DataTypeConverter<byte[], ?>>) converterInfo).isPresent()) {
            return null;
        }
        DataTypeConverter<byte[], ?> converter = ((Optional<DataTypeConverter<byte[], ?>>) converterInfo).get();

        return converter.convert(byte[].class, itemClass, sourceInstance);
    }

    @Nullable
    private Class<?> getListItemGenericClass(JavaBeanFieldMetadata targetFieldMetadata) {
        List<Class<?>> genericType = targetFieldMetadata.getGenericType();
        if (genericType == null || genericType.isEmpty()) {
            return null;
        }
        return genericType.get(0);
    }

    private static class DelegateDataTypeConverter {

        private final DataTypeConverterRegistry registry = new DefaultDataTypeConverterRegistry(false);

        public DelegateDataTypeConverter() {
            this.registry.clear();
            registry.registerConverter(new ByteArrayToIntegerDataTypeConverter());

            registry.registerConverter(new ByteArrayToShortDataTypeConverter());

            registry.registerConverter(new ByteArrayToBcdStringDataTypeConverter());

            registry.registerConverter(new ByteArrayToStringDataConverter());

            registry.registerConverter(new ByteArrayToByteDataTypeConverter());

            registry.registerConverter(new NoOpsByteArrayDataTypeConverter());

        }

        public Optional<DataTypeConverter<?, ?>> getConverter(ConvertibleMetadata key) {
            return this.registry.getConverter(key);
        }
    }
}
