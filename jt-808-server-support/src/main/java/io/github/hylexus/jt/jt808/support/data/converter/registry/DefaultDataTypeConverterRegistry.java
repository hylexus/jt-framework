package io.github.hylexus.jt.jt808.support.data.converter.registry;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.jt.jt808.support.data.converter.impl.*;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
public class DefaultDataTypeConverterRegistry implements DataTypeConverterRegistry {

    private final Map<ConvertibleMetadata, Jt808MsgDataTypeConverter<?>> converterMap = new ConcurrentHashMap<>();

    public DefaultDataTypeConverterRegistry(boolean autoRegisterDefaultConverter) {
        if (autoRegisterDefaultConverter) {
            registerDefaultConverter(this);
        }
    }

    public DefaultDataTypeConverterRegistry() {
        this(true);
    }

    static void registerDefaultConverter(DefaultDataTypeConverterRegistry registry) {

        registry.registerConverter(new ByteBufToBcdStringDataTypeConverter());

        registry.registerConverter(new ByteBufToByteArrayDataTypeConverter());

        registry.registerConverter(new ByteBufToByteBufDataTypeConverter());

        registry.registerConverter(new ByteBufToByteDataTypeConverter());

        registry.registerConverter(new ByteBufToIntegerDataTypeConverter());

        registry.registerConverter(new ByteBufToLongDataTypeConverter());

        registry.registerConverter(new ByteBufToShortDataTypeConverter());

        registry.registerConverter(new ByteBufToStringDataConverter());

    }

    @Override
    public void registerConverter(@NonNull Jt808MsgDataTypeConverter<?> converter) {
        for (ConvertibleMetadata convertibleType : converter.getConvertibleTypes()) {
            converterMap.put(convertibleType, converter);
        }
    }

    @Override
    public Optional<Jt808MsgDataTypeConverter<?>> getConverter(ConvertibleMetadata convertibleMetadata) {
        return Optional.ofNullable(this.converterMap.get(convertibleMetadata));
    }

    @Override
    public void clear() {
        this.converterMap.clear();
    }
}
