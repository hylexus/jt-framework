package io.github.hylexus.jt.jt808.support.data.serializer;

import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;

import java.util.Set;

public interface Jt808FieldSerializer<T> extends ReplaceableComponent {

    Set<ResponseMsgConvertibleMetadata> getSupportedTypes();

    /**
     * 该方法并没有废弃。
     * <p>
     * 但是在 {@code 2.1.1} 之后推荐使用重载过的 {@link #serialize(Object, MsgDataType, ByteBuf, Context)}，因为可以获取到 {@link Context}
     */
    void serialize(T object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException;

    /**
     * @since 2.1.1
     */
    default void serialize(T object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException {
        this.serialize(object, msgDataType, byteBuf);
    }

    interface Context {
        JavaBeanFieldMetadata fieldMetadata();
    }

    class DefaultInternalEncoderContext implements Context {
        private final JavaBeanFieldMetadata fieldMetadata;

        public DefaultInternalEncoderContext(JavaBeanFieldMetadata fieldMetadata) {
            this.fieldMetadata = fieldMetadata;
        }

        @Override
        public JavaBeanFieldMetadata fieldMetadata() {
            return this.fieldMetadata;
        }
    }

    @Override
    default int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    class PlaceholderFiledSerializer implements Jt808FieldSerializer<Object> {
        @Override
        public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
            throw new UnsupportedOperationException("This FieldSerializer only serves as a placeholder");
        }

        @Override
        public void serialize(Object object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
            throw new UnsupportedOperationException("This FieldSerializer only serves as a placeholder");
        }
    }
}