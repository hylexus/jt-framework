package io.github.hylexus.jt.codec;

import io.github.hylexus.jt.annotation.msg.SliceFrom;
import io.github.hylexus.jt.annotation.msg.SplittableField;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.utils.ReflectionUtils;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static io.github.hylexus.jt.annotation.msg.SliceFrom.DEFAULT_BIT_INDEX;

/**
 * @author hylexus
 * Created At 2019-10-04 7:32 下午
 */
@Slf4j
public class SplittableFieldDecoder {

    public void processSliceFromField(@NonNull Object instance) throws InstantiationException, IllegalAccessException {
        Class<?> cls = instance.getClass();
        JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getSliceableTypeList()) {
            SliceFrom annotation = fieldMetadata.getAnnotation(SliceFrom.class);
            if (annotation == null) {
                continue;
            }

            Optional<JavaBeanFieldMetadata> fieldInfo = beanMetadata.findFieldMedataByName(annotation.sourceFieldName());
            if (!fieldInfo.isPresent()) {
                log.error("No field found with name {} on {}", annotation.sourceFieldName(), cls);
                continue;
            }

            JavaBeanFieldMetadata sourceFieldMetadata = fieldInfo.get();
            if (!sourceFieldMetadata.isIntType()) {
                log.error("A field that to be spliced should have a type in (int,Integer) ");
                continue;
            }
            Object sourceValue = sourceFieldMetadata.getFieldValue(instance, false);
            if (sourceValue == null) {
                log.debug("source value is null, skip @SliceFrom processing on {}", fieldMetadata.getField());
                continue;
            }
            int intValue = (int) sourceValue;

            if (annotation.bitIndex() != DEFAULT_BIT_INDEX) {
                int sliceValue = Numbers.getBitAt(intValue, annotation.bitIndex());
                if (fieldMetadata.isIntType()) {
                    fieldMetadata.setFieldValue(instance, sliceValue);
                } else if (fieldMetadata.isBooleanType()) {
                    fieldMetadata.setFieldValue(instance, sliceValue == 1);
                }
            } else {
                if (annotation.startBitIndex() == DEFAULT_BIT_INDEX || annotation.endBitIndex() == DEFAULT_BIT_INDEX) {
                    log.error("SliceFrom.startBitIndex() or SliceFrom.endBitIndex() is null, skip @SliceFrom processing on {}", fieldMetadata.getField());
                    continue;
                }
                int sliceValue = Numbers.getBitRangeAsInt(intValue, annotation.startBitIndex(), annotation.endBitIndex());
                if (fieldMetadata.isIntType()) {
                    fieldMetadata.setFieldValue(instance, sliceValue);
                } else if (fieldMetadata.isBooleanType()) {
                    fieldMetadata.setFieldValue(instance, sliceValue == 1);
                }
            }
        }
    }

    public void processSplittableField(Object instance, JavaBeanFieldMetadata fieldMetadata, Object value) throws InstantiationException,
            IllegalAccessException {

        if (value == null) {
            log.debug("Original value is null, skip @SplittableField processing on field {}", fieldMetadata.getField());
            return;
        }

        SplittableField annotation = fieldMetadata.getAnnotation(SplittableField.class);
        if (annotation == null) {
            return;
        }

        if (!fieldMetadata.isIntType()) {
            log.error("A field marked by @SplittableField should be a type in (int , Integer)");
            return;
        }

        JavaBeanMetadata rawBeanMetadata = fieldMetadata.getRawBeanMetadata();
        Optional<JavaBeanFieldMetadata> targetFiledInfo = rawBeanMetadata.findFieldMedataByName(annotation.splitPropertyValueIntoNestedBeanField());
        if (!targetFiledInfo.isPresent()) {
            log.error("No target field found with name {} @Class {}", annotation.splitPropertyValueIntoNestedBeanField(), rawBeanMetadata.getOriginalClass());
            return;
        }

        JavaBeanFieldMetadata targetField = targetFiledInfo.get();
        Object targetFieldInstance = targetField.getFieldValue(instance, true);
        ReflectionUtils.setFieldValue(instance, targetField.getField(), targetFieldInstance);

        int intVal = (int) value;
        JavaBeanMetadata targetBeanInfo = JavaBeanMetadataUtils.getBeanMetadata(targetField.getFieldType());
        for (JavaBeanFieldMetadata metadata : targetBeanInfo.getFieldMetadataList()) {
            if (metadata.isAnnotationPresent(SplittableField.BitAt.class)) {
                SplittableField.BitAt bitInfo = metadata.getAnnotation(SplittableField.BitAt.class);
                int v = Numbers.getBitAt(intVal, bitInfo.bitIndex());
                if (metadata.isIntType()) {
                    metadata.setFieldValue(targetFieldInstance, v);
                } else if (metadata.isBooleanType()) {
                    metadata.setFieldValue(targetFieldInstance, v == 1);
                }
            } else if (metadata.isAnnotationPresent(SplittableField.BitAtRange.class)) {
                SplittableField.BitAtRange bitInfo = metadata.getAnnotation(SplittableField.BitAtRange.class);
                if (metadata.isIntType()) {
                    int v = Numbers.getBitRangeAsInt(intVal, bitInfo.startIndex(), bitInfo.endIndex());
                    metadata.setFieldValue(targetFieldInstance, v);
                }
            }

            if (metadata.isAnnotationPresent(SplittableField.class)) {
                Object val = metadata.getFieldValue(targetFieldInstance, true);
                this.processSplittableField(targetFieldInstance, metadata, val);
            }
        }
    }
}
