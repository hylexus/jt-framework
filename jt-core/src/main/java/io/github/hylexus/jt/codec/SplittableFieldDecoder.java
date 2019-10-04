package io.github.hylexus.jt.codec;

import io.github.hylexus.jt.annotation.msg.SplittableField;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.utils.ReflectionUtils;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-10-04 7:32 下午
 */
@Slf4j
public class SplittableFieldDecoder {

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
        Optional<JavaBeanFieldMetadata> targetFiledInfo = rawBeanMetadata.getFieldMedataByName(annotation.splitPropsIntoField());
        if (!targetFiledInfo.isPresent()) {
            log.error("No target field found with name {} @Class {}", annotation.splitPropsIntoField(), rawBeanMetadata.getOriginalClass());
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
                int v = Numbers.getBitAt(intVal, bitInfo.value());
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
