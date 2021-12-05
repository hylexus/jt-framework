package io.github.hylexus.jt.jt808.support.data.deserialize;

import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public interface Jt808FieldDeserializer<T> extends ReplaceableComponent {

    Set<RequestMsgConvertibleMetadata> getConvertibleTypes();

    T deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length);

    @Override
    default int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    class PlaceholderFieldDeserializer implements Jt808FieldDeserializer<Object> {
        @Override
        public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
            throw new UnsupportedOperationException("This FieldDeserializer only serves as a placeholder");
        }

        @Override
        public Object deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
            throw new UnsupportedOperationException("This FieldDeserializer only serves as a placeholder");
        }
    }
}
