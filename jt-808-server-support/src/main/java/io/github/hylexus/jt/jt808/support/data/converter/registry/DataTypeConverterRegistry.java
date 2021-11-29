package io.github.hylexus.jt.jt808.support.data.converter.registry;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.converter.Jt808MsgDataTypeConverter;
import lombok.NonNull;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface DataTypeConverterRegistry {

    void registerConverter(@NonNull Jt808MsgDataTypeConverter<?> converter);

    Optional<Jt808MsgDataTypeConverter<?>> getConverter(ConvertibleMetadata convertibleMetadata);

    void clear();
}
