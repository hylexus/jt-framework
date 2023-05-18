package io.github.hylexus.jt.jt808.support.data.meta;

import io.github.hylexus.jt.annotation.Internal;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * 内部用到的接口
 */
@Internal
public interface RequestFieldLengthExtractor {

    int extractFieldLength(
            EvaluationContext evaluationContext,
            Object containerInstance,
            RequestField annotation,
            MsgDataType jtDataType) throws Jt808AnnotationArgumentResolveException;

    static RequestFieldLengthExtractor createFor(Class<?> fieldType, MsgDataType dataType, RequestField annotation) {
        // 1. DataType.byteCount
        if (dataType.getByteCount() > 0) {
            return JT_DATA_TYPE_LENGTH_EXTRACTOR;
        }
        // 2. length()
        if (annotation.length() > 0) {
            return ANNOTATION_LENGTH_EXTRACTOR;
        }

        // 3. lengthExpression()
        if (StringUtils.isNotEmpty(annotation.lengthExpression())) {
            return ANNOTATION_LENGTH_EXPRESSION_EXTRACTOR;
        }

        // 4. lengthMethod()
        if (StringUtils.isEmpty(annotation.lengthMethod())) {
            throw new Jt808AnnotationArgumentResolveException("Can not determine field length [" + fieldType.getName() + "]");
        }

        return LENGTH_METHOD_EXTRACTOR;
    }

    RequestFieldLengthExtractor JT_DATA_TYPE_LENGTH_EXTRACTOR = (evaluationContext, containerInstance, annotation, jtDataType) -> jtDataType.getByteCount();
    RequestFieldLengthExtractor ANNOTATION_LENGTH_EXTRACTOR = (evaluationContext, containerInstance, annotation, jtDataType) -> annotation.length();
    RequestFieldLengthExtractor ANNOTATION_LENGTH_EXPRESSION_EXTRACTOR = new RequestFieldLengthExtractor() {
        private final ExpressionParser parser = new SpelExpressionParser();

        @Override
        public int extractFieldLength(
                EvaluationContext evaluationContext, Object containerInstance,
                RequestField annotation, MsgDataType jtDataType) throws Jt808AnnotationArgumentResolveException {

            final Number number = parser.parseExpression(annotation.lengthExpression()).getValue(evaluationContext, Number.class);
            if (number == null) {
                throw new Jt808AnnotationArgumentResolveException("Can not determine field length with Expression[" + annotation.lengthExpression() + "]");
            }
            return number.intValue();
        }
    };

    RequestFieldLengthExtractor LENGTH_METHOD_EXTRACTOR = (evaluationContext, containerInstance, annotation, jtDataType) -> {
        final Method lengthMethod = ReflectionUtils.findMethod(containerInstance.getClass(), annotation.lengthMethod());
        return ReflectionUtils.invokeMethod(containerInstance, lengthMethod);
    };
}
