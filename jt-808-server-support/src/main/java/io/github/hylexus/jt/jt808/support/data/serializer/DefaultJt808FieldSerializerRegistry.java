package io.github.hylexus.jt.jt808.support.data.serializer;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.impl.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class DefaultJt808FieldSerializerRegistry implements Jt808FieldSerializerRegistry {

    private final Map<ResponseMsgConvertibleMetadata, Jt808FieldSerializer<?>> converterMap = new ConcurrentHashMap<>();

    public DefaultJt808FieldSerializerRegistry(boolean autoRegisterDefaultConverter) {
        if (autoRegisterDefaultConverter) {
            registerDefaultConverter(this);
        }
    }

    static void registerDefaultConverter(DefaultJt808FieldSerializerRegistry registry) {
        registry.registerConverter(new IntegerFieldSerializer());

        registry.registerConverter(new ByteFieldSerializer());

        registry.registerConverter(new ShortFieldSerializer());

        registry.registerConverter(new LongFieldSerializer());

        registry.registerConverter(new StringFieldSerializer());

        registry.registerConverter(new ByteBufFieldSerializer());

        registry.registerConverter(new ByteArrayFieldSerializer());

        registry.registerConverter(new ByteBufContainerFieldSerializer());

        registry.registerConverter(new ByteArrayContainerFieldSerializer());

        registry.registerConverter(new BitOperatorFieldSerializer());
    }

    @Override
    public void registerConverter(@NonNull Jt808FieldSerializer<?> converter) {
        for (ResponseMsgConvertibleMetadata convertibleType : converter.getSupportedTypes()) {
            final Jt808FieldSerializer<?> old = converterMap.get(convertibleType);
            if (old != null && old.shouldBeReplacedBy(converter)) {
                log.warn("Jt808FieldSerializer [{}] has been replaced by [{}]", old.getClass().getName(), converter.getClass().getName());
            } else {
                converterMap.put(convertibleType, converter);
            }
        }
    }

    @Override
    public Optional<Jt808FieldSerializer<Object>> getConverter(ResponseMsgConvertibleMetadata convertibleMetadata) {
        @SuppressWarnings({"unchecked"}) final Jt808FieldSerializer<Object> serializer
                = (Jt808FieldSerializer<Object>) this.converterMap.get(convertibleMetadata);
        return Optional.ofNullable(serializer);
    }


    @Override
    public void clear() {
        this.converterMap.clear();
    }
}
