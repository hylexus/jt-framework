package io.github.hylexus.jt.jt808.support.data.deserialize;

import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author hylexus
 */
public interface Jt808FieldDeserializer<T> extends ReplaceableComponent {

    Set<RequestMsgConvertibleMetadata> getConvertibleTypes();

    /**
     * 该方法并没有废弃。
     * <p>
     * 但是在 {@code 2.1.1} 之后推荐使用重载过的 {@link #deserialize(ByteBuf, MsgDataType, int, int, Context)}，因为可以获取到 {@link Context}
     */
    T deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length);

    /**
     * @since 2.1.1
     */
    default T deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        return this.deserialize(byteBuf, msgDataType, start, length);
    }

    interface Context {
        JavaBeanFieldMetadata fieldMetadata();

        /**
         * @since 2.1.4
         */
        @Nullable
        Jt808AnnotationBasedDecoder delegate();

        /**
         * @since 2.1.4
         */
        @Nullable
        Jt808Request request();
    }

    class DefaultInternalDecoderContext implements Context {

        private final JavaBeanFieldMetadata fieldMetadata;
        private final Jt808AnnotationBasedDecoder delegate;
        private final Jt808Request request;

        public DefaultInternalDecoderContext(JavaBeanFieldMetadata fieldMetadata, Jt808AnnotationBasedDecoder delegate, Jt808Request request) {
            this.fieldMetadata = fieldMetadata;
            this.delegate = delegate;
            this.request = request;
        }

        @Override
        public JavaBeanFieldMetadata fieldMetadata() {
            return this.fieldMetadata;
        }

        @Override
        public Jt808AnnotationBasedDecoder delegate() {
            return this.delegate;
        }

        @Override
        public Jt808Request request() {
            return this.request;
        }
    }

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
