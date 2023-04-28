package io.github.hylexus.jt.jt808.support.data.deserialize;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.impl.*;
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
public class DefaultJt808FieldDeserializerRegistry implements Jt808FieldDeserializerRegistry {

    private final Map<RequestMsgConvertibleMetadata, Jt808FieldDeserializer<?>> converterMap = new ConcurrentHashMap<>();

    public DefaultJt808FieldDeserializerRegistry(boolean autoRegisterDefaultConverter) {
        if (autoRegisterDefaultConverter) {
            registerDefaultConverter(this);
        }
    }

    static void registerDefaultConverter(DefaultJt808FieldDeserializerRegistry registry) {

        registry.registerConverter(new BcdFieldDeserializer());

        registry.registerConverter(new ByteArrayFieldDeserializer());

        registry.registerConverter(new ByteBufFieldDeserializer());

        registry.registerConverter(new ByteFieldDeserializer());

        registry.registerConverter(new IntegerFieldDeserializer());

        registry.registerConverter(new LongFieldDeserializer());

        registry.registerConverter(new ShortFieldDeserializer());

        registry.registerConverter(new StringFieldDeserializer());

        registry.registerConverter(new ByteArrayContainerFieldDeserializer());

        registry.registerConverter(new ByteBufContainerFieldDeserializer());

        registry.registerConverter(new BitOperatorFieldDeserializer());

    }

    @Override
    public void registerConverter(@NonNull Jt808FieldDeserializer<?> converter) {
        for (RequestMsgConvertibleMetadata convertibleType : converter.getConvertibleTypes()) {
            final Jt808FieldDeserializer<?> old = converterMap.get(convertibleType);
            if (old != null && old.shouldBeReplacedBy(converter)) {
                log.warn("Jt808FieldDeserializer [{}] has been replaced by [{}]", old.getClass().getName(), converter.getClass().getName());
                converterMap.put(convertibleType, converter);
            } else {
                converterMap.put(convertibleType, converter);
            }
        }
    }

    @Override
    public Optional<Jt808FieldDeserializer<?>> getConverter(RequestMsgConvertibleMetadata convertibleMetadata) {
        return Optional.ofNullable(this.converterMap.get(convertibleMetadata));
    }

    @Override
    public void clear() {
        this.converterMap.clear();
    }
}
