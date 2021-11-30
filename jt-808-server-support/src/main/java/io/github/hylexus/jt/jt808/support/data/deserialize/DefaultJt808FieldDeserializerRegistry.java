package io.github.hylexus.jt.jt808.support.data.deserialize;

import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.impl.*;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
public class DefaultJt808FieldDeserializerRegistry implements Jt808FieldDeserializerRegistry {

    private final Map<RequestMsgConvertibleMetadata, Jt808FieldDeserializer<?>> converterMap = new ConcurrentHashMap<>();

    public DefaultJt808FieldDeserializerRegistry(boolean autoRegisterDefaultConverter) {
        if (autoRegisterDefaultConverter) {
            registerDefaultConverter(this);
        }
    }

    public DefaultJt808FieldDeserializerRegistry() {
        this(true);
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

    }

    @Override
    public void registerConverter(@NonNull Jt808FieldDeserializer<?> converter) {
        for (RequestMsgConvertibleMetadata convertibleType : converter.getConvertibleTypes()) {
            converterMap.put(convertibleType, converter);
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
