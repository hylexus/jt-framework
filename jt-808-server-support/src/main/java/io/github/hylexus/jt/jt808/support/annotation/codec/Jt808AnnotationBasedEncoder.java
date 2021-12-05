package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.jt808.support.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.jt808.support.utils.ReflectionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;

import static io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata.forJt808ResponseMsgDataType;
import static java.util.Objects.requireNonNull;

public class Jt808AnnotationBasedEncoder {

    private final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    private final Map<Class<? extends Jt808FieldSerializer<?>>, Jt808FieldSerializer<?>> converterMapping = new HashMap<>();
    private final Jt808FieldSerializerRegistry fieldSerializerRegistry;

    public Jt808AnnotationBasedEncoder(Jt808FieldSerializerRegistry fieldSerializerRegistry) {
        this.fieldSerializerRegistry = fieldSerializerRegistry;
    }

    // TODO annotation properties...
    public Jt808Response encode(Object responseMsg, Jt808Session session) {
        final Class<?> entityClass = responseMsg.getClass();
        final Jt808ResponseMsgBody annotation = requireNonNull(
                AnnotationUtils.findAnnotation(entityClass, Jt808ResponseMsgBody.class),
                "[" + entityClass.getSimpleName() + "] should be marked by @" + Jt808ResponseMsgBody.class.getSimpleName());

        final ByteBuf respBody = this.encodeMsgBody(responseMsg);
        return Jt808Response.newBuilder()
                .body(respBody)
                .version(session.getProtocolVersion())
                .msgId(annotation.respMsgId())
                .terminalId(session.getTerminalId())
                .flowId(session.getCurrentFlowId())
                .build();
    }

    public ByteBuf encodeMsgBody(Object responseMsgInstance) {
        final Class<?> entityClass = responseMsgInstance.getClass();
        final ByteBuf byteBuf = allocator.buffer();
        doEncode(responseMsgInstance, entityClass, byteBuf);
        return byteBuf;
    }

    private void doEncode(Object responseMsgInstance, Class<?> entityClass, ByteBuf byteBuf) {
        final JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(entityClass);
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {
            if (fieldMetadata.isAnnotationPresent(ResponseField.class)) {
                this.processBasicRespField(entityClass, responseMsgInstance, fieldMetadata, byteBuf);
            }
        }
    }

    private void processBasicRespField(Class<?> entityClass, Object responseMsgInstance, JavaBeanFieldMetadata fieldMetadata, ByteBuf byteBuf) {
        final ResponseField responseFieldAnnotation = fieldMetadata.getAnnotation(ResponseField.class);
        final MsgDataType jtDataType = responseFieldAnnotation.dataType();
        final Object fieldValue = fieldMetadata.getFieldValue(responseMsgInstance, false);

        final Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass = responseFieldAnnotation.customerFieldSerializerClass();
        if (customerFieldSerializerClass != Jt808FieldSerializer.PlaceholderFiledSerializer.class) {
            final Jt808FieldSerializer<Object> fieldSerializer = this.getFieldSerializer(customerFieldSerializerClass);
            fieldSerializer.serialize(fieldValue, jtDataType, byteBuf);
            return;
        }
        if (jtDataType == MsgDataType.LIST) {
            // TODO list serialize
            if (!(fieldValue instanceof Iterable)) {
                throw new JtIllegalArgumentException(fieldMetadata.getFieldType().getName() + " should be Iterable");
            }
            final Class<?> itemClass = fieldMetadata.getGenericType().get(0);
            for (Object item : ((Iterable<?>) fieldValue)) {
                this.doEncode(item, itemClass, byteBuf);
            }
            return;
        }

        final Class<?> fieldType = fieldMetadata.getFieldType();
        if (!jtDataType.getExpectedTargetClassType().contains(fieldType)) {
            throw new JtIllegalArgumentException("Can not convert [" + fieldType.getName() + "] to [" + jtDataType + "]");
        }

        final Jt808FieldSerializer<Object> fieldSerializer = fieldSerializerRegistry.getConverter(forJt808ResponseMsgDataType(fieldType, jtDataType))
                .orElseThrow(() -> new Jt808FieldSerializerException("Can not serialize [" + fieldMetadata.getField() + "]"));

        fieldSerializer.serialize(fieldValue, jtDataType, byteBuf);
    }

    @SuppressWarnings("unchecked")
    private Jt808FieldSerializer<Object> getFieldSerializer(
            Class<? extends Jt808FieldSerializer<?>> converterClass) throws Jt808AnnotationArgumentResolveException {

        Jt808FieldSerializer<Object> converter = (Jt808FieldSerializer<Object>) converterMapping.get(converterClass);
        if (converter == null) {
            synchronized (this) {
                if ((converter = (Jt808FieldSerializer<Object>) converterMapping.get(converterClass)) == null) {
                    converter = (Jt808FieldSerializer<Object>) ReflectionUtils.createInstance(converterClass);
                    converterMapping.put(converterClass, converter);
                }
            }
        }
        return converter;
    }
}
