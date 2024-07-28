package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanMetadata;
import io.github.hylexus.jt.jt808.support.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom.DEFAULT_BIT_INDEX;


/**
 * Created At 2019-10-06 5:18 下午
 *
 * @author hylexus
 */
@Slf4j
public class SlicedFromAnnotationDecoder {
    /**
     * 一次性处理当前instance的所有被 {@link SlicedFrom} 标记的Field
     */
    public void processAllSlicedFromAnnotatedField(@NonNull Object instance) throws IllegalAccessException {
        Class<?> cls = instance.getClass();
        JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getSliceFromSupportedFieldList()) {
            SlicedFrom annotation = fieldMetadata.getAnnotation(SlicedFrom.class);
            if (annotation == null) {
                continue;
            }

            Optional<JavaBeanFieldMetadata> sourceFieldInfo = beanMetadata.findFieldMedataByName(annotation.sourceFieldName());
            if (Jdk8Adapter.optionalIsEmpty(sourceFieldInfo)) {
                log.error("No field found with name {} on {}", annotation.sourceFieldName(), cls);
                continue;
            }

            JavaBeanFieldMetadata sourceFieldMetadata = sourceFieldInfo.get();
            if (!JavaBeanMetadataUtils.isSlicedType(sourceFieldMetadata.getFieldType())) {
                log.error("A field that to be spliced should have a type in {} --> {} ", JavaBeanMetadataUtils.SLICED_TYPE, sourceFieldMetadata.getField());
                continue;
            }
            Object sourceValue = sourceFieldMetadata.getFieldValue(instance, false);
            if (sourceValue == null) {
                log.debug("source value is null, skip @SlicedFrom processing on {}", fieldMetadata.getField());
                continue;
            }
            long intValue = ((Number) sourceValue).longValue();

            setFieldValue(instance, fieldMetadata, annotation, intValue);
        }
    }

    private void setFieldValue(Object instance, JavaBeanFieldMetadata fieldMetadata, SlicedFrom annotation, long intValue) throws IllegalAccessException {
        if (annotation.bitIndex() != DEFAULT_BIT_INDEX) {
            int sliceValue = Numbers.getBitAt(intValue, annotation.bitIndex());
            setValue(instance, fieldMetadata, sliceValue);
        } else {
            if (annotation.startBitIndex() == DEFAULT_BIT_INDEX || annotation.endBitIndex() == DEFAULT_BIT_INDEX) {
                log.error("SliceFrom.startBitIndex() or SliceFrom.endBitIndex() is null, skip @SliceFrom processing on {}", fieldMetadata.getField());
                return;
            }
            long sliceValue = Numbers.getBitRangeAsLong(intValue, annotation.startBitIndex(), annotation.endBitIndex());
            setValue(instance, fieldMetadata, sliceValue);
        }
    }

    private void setValue(Object instance, JavaBeanFieldMetadata fieldMetadata, long targetValue) throws IllegalAccessException {
        if (fieldMetadata.isIntType()) {
            fieldMetadata.setFieldValue(instance, (int) targetValue);
        } else if (fieldMetadata.isBooleanType()) {
            fieldMetadata.setFieldValue(instance, targetValue == 1L);
        } else if (fieldMetadata.isByteType()) {
            fieldMetadata.setFieldValue(instance, (byte) targetValue);
        } else if (fieldMetadata.isShortType()) {
            fieldMetadata.setFieldValue(instance, (short) targetValue);
        } else if (fieldMetadata.isLongType()) {
            fieldMetadata.setFieldValue(instance, targetValue);
        }
    }
}
