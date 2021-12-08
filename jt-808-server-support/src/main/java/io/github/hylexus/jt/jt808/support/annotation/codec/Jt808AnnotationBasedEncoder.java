package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

import static io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata.forJt808ResponseMsgDataType;
import static java.util.Objects.requireNonNull;

public class Jt808AnnotationBasedEncoder {

    private final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final Map<Class<? extends Jt808FieldSerializer<?>>, Jt808FieldSerializer<?>> converterMapping = new HashMap<>();
    private final Jt808FieldSerializerRegistry fieldSerializerRegistry;

    public Jt808AnnotationBasedEncoder(Jt808FieldSerializerRegistry fieldSerializerRegistry) {
        this.fieldSerializerRegistry = fieldSerializerRegistry;
    }

    // TODO annotation properties...
    public Jt808Response encode(Jt808Request request, Object responseMsg, Jt808Session session) {
        return this.encode(request, responseMsg, session, session.getCurrentFlowId());
    }

    public Jt808Response encode(Object responseMsg, Jt808Session session, int serverFlowId) {
        return this.encode(null, responseMsg, session, serverFlowId);
    }

    public Jt808Response encode(Jt808Request request, Object responseMsg, Jt808Session session, int serverFlowId) {
        final Class<?> entityClass = responseMsg.getClass();
        final Jt808ResponseBody annotation = requireNonNull(
                AnnotationUtils.findAnnotation(entityClass, Jt808ResponseBody.class),
                "[" + entityClass.getSimpleName() + "] should be marked by @" + Jt808ResponseBody.class.getSimpleName());

        final ByteBuf respBody = this.encodeMsgBody(request, responseMsg, session);
        return Jt808Response.newBuilder()
                .body(respBody)
                .version(session.getProtocolVersion())
                .msgId(annotation.respMsgId())
                .terminalId(session.getTerminalId())
                .flowId(serverFlowId)
                .build();
    }

    private ByteBuf encodeMsgBody(Jt808Request request, Object instance, Jt808Session session) {
        final Class<?> entityClass = instance.getClass();
        final ByteBuf byteBuf = allocator.buffer();

        doEncode(instance, entityClass, byteBuf, request, session);

        return byteBuf;
    }

    private void doEncode(Object instance, Class<?> entityClass, ByteBuf byteBuf, Jt808Request request, Jt808Session session) {
        final JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(entityClass);
        final EvaluationContext evaluationContext = new StandardEvaluationContext(instance);
        evaluationContext.setVariable("this", instance);
        if (request != null) {
            evaluationContext.setVariable("request", request);
            evaluationContext.setVariable("header", request.header());
        }
        if (session != null) {
            evaluationContext.setVariable("session", session);
        }
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {
            if (fieldMetadata.isAnnotationPresent(ResponseField.class)) {
                this.processBasicRespField(instance, fieldMetadata, byteBuf, request, session, evaluationContext);
            }
        }
    }

    private void processBasicRespField(
            Object instance, JavaBeanFieldMetadata fieldMetadata,
            ByteBuf byteBuf, Jt808Request request, Jt808Session session,
            EvaluationContext evaluationContext) {

        final ResponseField fieldAnnotation = fieldMetadata.getAnnotation(ResponseField.class);
        final MsgDataType jtDataType = fieldAnnotation.dataType();
        final Object fieldValue = fieldMetadata.getFieldValue(instance, false);
        final String conditionalExpression = fieldAnnotation.conditionalOn();
        if (StringUtils.isNotEmpty(conditionalExpression) && this.isConditionNotMatch(conditionalExpression, evaluationContext)) {
            return;
        }
        // 1. 优先使用自定义转换器
        final Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass = fieldAnnotation.customerFieldSerializerClass();
        if (customerFieldSerializerClass != Jt808FieldSerializer.PlaceholderFiledSerializer.class) {
            final Jt808FieldSerializer<Object> fieldSerializer = this.getFieldSerializer(customerFieldSerializerClass);
            fieldSerializer.serialize(fieldValue, jtDataType, byteBuf);
            return;
        }
        // 2. LIST
        if (jtDataType == MsgDataType.LIST) {
            if (!(fieldValue instanceof Iterable)) {
                throw new JtIllegalArgumentException(fieldMetadata.getFieldType().getName() + " should be Iterable");
            }
            final Class<?> itemClass = fieldMetadata.getGenericType().get(0);
            for (Object item : ((Iterable<?>) fieldValue)) {
                this.doEncode(item, itemClass, byteBuf, request, session);
            }
            return;
        }
        // 3. 内嵌对象
        if (jtDataType == MsgDataType.OBJECT) {
            final Class<?> fieldType = fieldMetadata.getFieldType();
            this.doEncode(fieldValue, fieldType, byteBuf, request, session);
            return;
        }

        final Class<?> fieldType = fieldMetadata.getFieldType();
        if (!jtDataType.getExpectedTargetClassType().contains(fieldType)) {
            throw new JtIllegalArgumentException("Can not convert [" + fieldType.getName() + "] to [" + jtDataType + "]");
        }

        // 4. 内置转换器
        final Jt808FieldSerializer<Object> fieldSerializer = fieldSerializerRegistry.getConverter(forJt808ResponseMsgDataType(fieldType, jtDataType))
                .orElseThrow(() -> new Jt808FieldSerializerException("Can not serialize [" + fieldMetadata.getField() + "]"));

        fieldSerializer.serialize(fieldValue, jtDataType, byteBuf);
    }

    private boolean isConditionNotMatch(String expression, EvaluationContext evaluationContext) {
        final Boolean match = parser.parseExpression(expression).getValue(evaluationContext, boolean.class);
        return Boolean.FALSE.equals(match);
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
