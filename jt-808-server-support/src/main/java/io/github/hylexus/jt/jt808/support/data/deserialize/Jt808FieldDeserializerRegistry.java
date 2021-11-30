package io.github.hylexus.jt.jt808.support.data.deserialize;

import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import lombok.NonNull;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt808FieldDeserializerRegistry {

    void registerConverter(@NonNull Jt808FieldDeserializer<?> converter);

    Optional<Jt808FieldDeserializer<?>> getConverter(RequestMsgConvertibleMetadata convertibleMetadata);

    void clear();
}
