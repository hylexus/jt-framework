package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
public class DefaulJt808FieldSerializerRegistry implements Jt808FieldSerializerRegistry {

    private final Map<ResponseMsgConvertibleMetadata, Jt808FieldSerializer<?>> converterMap = new ConcurrentHashMap<>();

    public DefaulJt808FieldSerializerRegistry(boolean autoRegisterDefaultConverter) {
        if (autoRegisterDefaultConverter) {
            registerDefaultConverter(this);
        }
    }

    public DefaulJt808FieldSerializerRegistry() {
        this(true);
    }

    static void registerDefaultConverter(DefaulJt808FieldSerializerRegistry registry) {
        registry.registerConverter(new IntegerFieldJt808FieldSerializer());

        registry.registerConverter(new ByteFieldJt808FieldSerializer());

        registry.registerConverter(new ShortFieldJt808FieldSerializer());

        registry.registerConverter(new LongFieldJt808FieldSerializer());
    }

    @Override
    public void registerConverter(@NonNull Jt808FieldSerializer<?> converter) {
        for (ResponseMsgConvertibleMetadata convertibleType : converter.getSupportedTypes()) {
            converterMap.put(convertibleType, converter);
        }
    }

    @Override
    public Optional<Jt808FieldSerializer<Object>> getConverter(ResponseMsgConvertibleMetadata convertibleMetadata) {
        @SuppressWarnings("unckecked") final Jt808FieldSerializer<Object> serializer = (Jt808FieldSerializer<Object>) this.converterMap.get(convertibleMetadata);
        return Optional.ofNullable(serializer);
    }


    @Override
    public void clear() {
        this.converterMap.clear();
    }
}
