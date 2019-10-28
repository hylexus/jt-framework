package io.github.hylexus.jt.data.converter.registry;

import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import lombok.NonNull;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-10-28 7:44 下午
 */
public interface DataTypeConverterRegistry {

    <S, T> void registerConverter(Class<S> sourceType, Class<T> targetType, DataTypeConverter<? extends S, ? extends T> converter);

    void registerConverter(@NonNull DataTypeConverter<?, ?> converter);

    <S, T> Optional<DataTypeConverter<S, T>> getConverter(Class<S> sourceType, Class<T> targetType);

    Optional<DataTypeConverter<?, ?>> getConverter(ConvertibleMetadata convertibleMetadata);
}
