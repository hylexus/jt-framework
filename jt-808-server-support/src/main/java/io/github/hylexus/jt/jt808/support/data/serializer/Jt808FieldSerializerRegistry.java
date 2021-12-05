package io.github.hylexus.jt.jt808.support.data.serializer;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import lombok.NonNull;
import org.springframework.core.Ordered;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt808FieldSerializerRegistry {

    void registerConverter(@NonNull Jt808FieldSerializer<?> converter);

    Optional<Jt808FieldSerializer<Object>> getConverter(ResponseMsgConvertibleMetadata convertibleMetadata);

    void clear();

    interface Jt808FieldSerializerRegistryCustomizer extends OrderedComponent, Ordered {

        void customize(Jt808FieldSerializerRegistry registry);

        @Override
        default int getOrder() {
            return DEFAULT_ORDER;
        }
    }
}
