package io.github.hylexus.jt.jt808.support.data.meta;

import io.github.hylexus.jt.annotation.Internal;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
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
public interface RequestFieldOffsetExtractor {

    int extractFieldOffset(
            EvaluationContext evaluationContext,
            Object containerInstance,
            RequestField annotation,
            JavaBeanFieldMetadata fieldMetadata) throws Jt808AnnotationArgumentResolveException;

    static RequestFieldOffsetExtractor createFor(JavaBeanFieldMetadata fieldMetadata, RequestField annotation) {
        if (annotation.startIndex() >= 0) {
            return START_INDEX_EXTRACTOR;
        }
        if (StringUtils.isNotEmpty(annotation.startIndexExpression())) {
            return START_INDEX_EXPRESSION_EXTRACTOR;
        }
        if (!StringUtils.isEmpty(annotation.startIndexMethod())) {
            return START_INDEX_METHOD_SUPPLIER;
        }

        return PLACEHOLDER;
    }

    RequestFieldOffsetExtractor PLACEHOLDER = (evaluationContext, containerInstance, annotation, fieldMetadata) -> -2;
    RequestFieldOffsetExtractor START_INDEX_EXTRACTOR = (evaluationContext, containerInstance, annotation, fieldMetadata) -> annotation.startIndex();

    RequestFieldOffsetExtractor START_INDEX_EXPRESSION_EXTRACTOR = new RequestFieldOffsetExtractor() {
        private final ExpressionParser parser = new SpelExpressionParser();

        @Override
        public int extractFieldOffset(
                EvaluationContext evaluationContext, Object containerInstance,
                RequestField annotation, JavaBeanFieldMetadata fieldMetadata) throws Jt808AnnotationArgumentResolveException {

            final Number number = this.parser.parseExpression(annotation.startIndexExpression()).getValue(evaluationContext, Number.class);
            if (number == null) {
                throw new Jt808AnnotationArgumentResolveException(
                        "Can not determine field[" + fieldMetadata.getField().getName() + "] startIndex with Expression["
                                + annotation.lengthExpression() + "]");
            }
            return number.intValue();
        }
    };

    RequestFieldOffsetExtractor START_INDEX_METHOD_SUPPLIER = (evaluationContext, containerInstance, annotation, fieldMetadata) -> {
        final Method lengthMethod = ReflectionUtils.findMethod(containerInstance.getClass(), annotation.lengthMethod());
        return ReflectionUtils.invokeMethod(containerInstance, lengthMethod);
    };
}