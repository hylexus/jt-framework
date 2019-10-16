package io.github.hylexus.jt.codec;

import io.github.hylexus.jt.annotation.msg.req.slice.SlicedFrom;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static io.github.hylexus.jt.annotation.msg.req.slice.SlicedFrom.DEFAULT_BIT_INDEX;

/**
 * @author hylexus
 * Created At 2019-10-06 5:18 下午
 */
@Slf4j
public class SlicedFromDecoder {
    /**
     * 一次性处理当前instance的所有被 {@link SlicedFrom} 标记的Field
     */
    public void processAllSlicedFromField(@NonNull Object instance) throws InstantiationException, IllegalAccessException {
        Class<?> cls = instance.getClass();
        JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getSliceFromSupportedFieldList()) {
            SlicedFrom annotation = fieldMetadata.getAnnotation(SlicedFrom.class);
            if (annotation == null) {
                continue;
            }

            Optional<JavaBeanFieldMetadata> sourceFieldInfo = beanMetadata.findFieldMedataByName(annotation.sourceFieldName());
            if (!sourceFieldInfo.isPresent()) {
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
            int intValue = ((Number) sourceValue).intValue();

            setFieldValue(instance, fieldMetadata, annotation, intValue);
        }
    }

    private void setFieldValue(Object instance, JavaBeanFieldMetadata fieldMetadata, SlicedFrom annotation, int intValue) throws IllegalAccessException {
        if (annotation.bitIndex() != DEFAULT_BIT_INDEX) {
            int sliceValue = Numbers.getBitAt(intValue, annotation.bitIndex());
            setValue(instance, fieldMetadata, sliceValue);
        } else {
            if (annotation.startBitIndex() == DEFAULT_BIT_INDEX || annotation.endBitIndex() == DEFAULT_BIT_INDEX) {
                log.error("SliceFrom.startBitIndex() or SliceFrom.endBitIndex() is null, skip @SliceFrom processing on {}", fieldMetadata.getField());
                return;
            }
            int sliceValue = Numbers.getBitRangeAsInt(intValue, annotation.startBitIndex(), annotation.endBitIndex());
            setValue(instance, fieldMetadata, sliceValue);
        }
    }

    private void setValue(Object instance, JavaBeanFieldMetadata fieldMetadata, int targetValue) throws IllegalAccessException {
        if (fieldMetadata.isIntType()) {
            fieldMetadata.setFieldValue(instance, targetValue);
        } else if (fieldMetadata.isBooleanType()) {
            fieldMetadata.setFieldValue(instance, targetValue == 1);
        }
    }
}
