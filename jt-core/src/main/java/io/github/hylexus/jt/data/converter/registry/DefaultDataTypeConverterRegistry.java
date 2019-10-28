package io.github.hylexus.jt.data.converter.registry;

import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import io.github.hylexus.jt.data.converter.impl.*;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 * Created At 2019-10-28 7:52 下午
 */
public class DefaultDataTypeConverterRegistry implements DataTypeConverterRegistry {

    private final Map<ConvertibleMetadata, DataTypeConverter<?, ?>> converterMap = new ConcurrentHashMap<>();

    public DefaultDataTypeConverterRegistry() {
        registerDefaultConverter(this);
    }

    static void registerDefaultConverter(DefaultDataTypeConverterRegistry registry) {

        registry.registerConverter(new ByteArrayToIntegerDataTypeConverter());

        registry.registerConverter(new ByteArrayToShortDataTypeConverter());

        registry.registerConverter(new ByteArrayToBcdStringDataTypeConverter());

        registry.registerConverter(new ByteArrayToStringDataConverter());

        registry.registerConverter(new ByteArrayToByteDataTypeConverter());
        
        registry.registerConverter(new NoOpsByteArrayDataTypeConverter());
    }

    @Override
    public <S, T> void registerConverter(Class<S> sourceType, Class<T> targetType, DataTypeConverter<? extends S, ? extends T> converter) {
        ConvertibleMetadata key = new ConvertibleMetadata(sourceType, targetType);
        converterMap.put(key, converter);
    }

    @Override
    public void registerConverter(@NonNull DataTypeConverter<?, ?> converter) {
        for (ConvertibleMetadata convertibleType : converter.getConvertibleTypes()) {
            converterMap.put(convertibleType, converter);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S, T> Optional<DataTypeConverter<S, T>> getConverter(Class<S> sourceType, Class<T> targetType) {
        DataTypeConverter<S, T> converter = (DataTypeConverter<S, T>) this.converterMap.get(new ConvertibleMetadata(sourceType, targetType));
        return Optional.ofNullable(converter);
    }

    @Override
    public Optional<DataTypeConverter<?, ?>> getConverter(ConvertibleMetadata convertibleMetadata) {
        return Optional.ofNullable(this.converterMap.get(convertibleMetadata));
    }
}
