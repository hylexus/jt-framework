package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.jt808.support.annotation.msg.Jt808ResponseMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.basic.BasicField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.impl.DefaulJt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.jt808.support.utils.JavaBeanMetadataUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.springframework.core.annotation.AnnotationUtils;

import static io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata.forJt808ResponseMsgDataType;
import static java.util.Objects.requireNonNull;

public class Jt808AnnotationBasedEncoder {

    private final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    private final Jt808FieldSerializerRegistry fieldSerializerRegistry = new DefaulJt808FieldSerializerRegistry();

    public ByteBuf encode(Object responseMsgInstance) {
        final Class<?> entityClass = responseMsgInstance.getClass();
        final Jt808ResponseMsgBody annotation = requireNonNull(
                AnnotationUtils.findAnnotation(entityClass, Jt808ResponseMsgBody.class),
                entityClass.getSimpleName() + " must be annotated with " + Jt808ResponseMsgBody.class);

        // TODO header+checkSum+body
        final ByteBuf byteBuf = allocator.buffer();
        final JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(entityClass);
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {
            if (fieldMetadata.isAnnotationPresent(BasicField.class)) {
                this.processBasicField(entityClass, responseMsgInstance, fieldMetadata, byteBuf);
            }
        }
        return byteBuf;
    }

    private void processBasicField(Class<?> entityClass, Object responseMsgInstance, JavaBeanFieldMetadata fieldMetadata, ByteBuf byteBuf) {
        final BasicField basicFieldAnnotation = fieldMetadata.getAnnotation(BasicField.class);
        final MsgDataType jtDataType = basicFieldAnnotation.dataType();
        if (jtDataType == MsgDataType.LIST) {
            // TODO list serialize
            return;
        }

        final Class<?> fieldType = fieldMetadata.getFieldType();
        if (!jtDataType.getExpectedTargetClassType().contains(fieldType)) {
            throw new JtIllegalArgumentException("Can not convert [" + fieldType.getName() + "] to [" + jtDataType + "]");
        }

        final Jt808FieldSerializer<Object> fieldSerializer = fieldSerializerRegistry.getConverter(forJt808ResponseMsgDataType(fieldType, jtDataType))
                .orElseThrow(() -> new Jt808FieldSerializerException("Can not serialize [" + fieldMetadata.getField().getName() + "]"));

        final Object fieldValue = fieldMetadata.getFieldValue(responseMsgInstance, false);
        fieldSerializer.serialize(fieldValue, jtDataType, byteBuf);
    }
}
