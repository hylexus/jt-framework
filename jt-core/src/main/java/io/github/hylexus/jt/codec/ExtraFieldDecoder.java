package io.github.hylexus.jt.codec;

import io.github.hylexus.jt.annotation.msg.extra.ExtraField;
import io.github.hylexus.jt.annotation.msg.extra.ExtraMsgBody;
import io.github.hylexus.jt.data.msg.NestedFieldMappingInfo;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.github.hylexus.jt.utils.ReflectionUtils.populateBasicField;

/**
 * @author hylexus
 * Created At 2019-10-01 7:39 下午
 */
public class ExtraFieldDecoder {

    private static final ConcurrentMap<Class<?>, ConcurrentMap<Integer, NestedFieldMappingInfo>> cache = new ConcurrentHashMap<>();
    private SplittableFieldDecoder splittableFieldDecoder = new SplittableFieldDecoder();
    private SlicedFromDecoder slicedFromDecoder = new SlicedFromDecoder();

    public void decodeExtraField(
            byte[] bytes, int startIndex, int length, Object instance, JavaBeanFieldMetadata fieldMetadata)
            throws IllegalAccessException, InstantiationException {

        final ExtraField extraFieldAnnotation = fieldMetadata.getAnnotation(ExtraField.class);

        final Class<?> extraFieldClass = fieldMetadata.getFieldType();

        Object extraFieldInstance = extraFieldClass.newInstance();
        fieldMetadata.getField().setAccessible(true);
        fieldMetadata.getField().set(instance, extraFieldInstance);

        Map<Integer, NestedFieldMappingInfo> mappingInfo = getMappingInfo(extraFieldClass);

        decodeNestedField(
                bytes, startIndex, length, extraFieldInstance, mappingInfo,
                extraFieldAnnotation.byteCountOfMsgId(), extraFieldAnnotation.byteCountOfContentLength()
        );
    }

    private void decodeNestedField(
            byte[] bytes, int startIndex, int length, Object instance, Map<Integer, NestedFieldMappingInfo> mappingInfo,
            int byteCountOfMsgId, int byteCountOfContentLength) throws IllegalAccessException, InstantiationException {

        int readerIndex = startIndex;
        while (readerIndex < length) {
            int msgId = IntBitOps.intFromBytes(bytes, readerIndex, byteCountOfMsgId);
            readerIndex += byteCountOfMsgId;

            final NestedFieldMappingInfo info = mappingInfo.get(msgId);
            if (info == null) {
                continue;
            }

            int bodyLength = IntBitOps.intFromBytes(bytes, readerIndex, byteCountOfContentLength);
            readerIndex += byteCountOfContentLength;

            byte[] bodyBytes = Bytes.subSequence(bytes, readerIndex, bodyLength);
            readerIndex += bodyLength;

            if (info.isNestedExtraField()) {
                Map<Integer, NestedFieldMappingInfo> map = getMappingInfo(info.getFieldMetadata().getFieldType());

                ExtraMsgBody ex = info.getFieldMetadata().getFieldType().getAnnotation(ExtraMsgBody.class);

                Object newInstance = info.getFieldMetadata().getFieldType().newInstance();
                info.getFieldMetadata().setFieldValue(instance, newInstance);

                decodeNestedField(bodyBytes, 0, bodyBytes.length, newInstance, map, ex.byteCountOfMsgId(), ex.byteCountOfContentLength());
            } else {
                Object value = populateBasicField(bodyBytes, instance, info.getFieldMetadata(), info.getDataType(), 0,
                        bodyBytes.length);

                splittableFieldDecoder.processSplittableField(instance, info.getFieldMetadata(), value);
            }
        }
        slicedFromDecoder.processAllSlicedFromField(instance);
    }

    private Map<Integer, NestedFieldMappingInfo> getMappingInfo(Class<?> cls) {
        final ConcurrentMap<Integer, NestedFieldMappingInfo> cachedMappingInfo = cache.get(cls);
        if (cachedMappingInfo != null) {
            return cachedMappingInfo;
        }

        final ConcurrentMap<Integer, NestedFieldMappingInfo> newInfo = buildNestedMappingInfo(cls);
        cache.put(cls, newInfo);
        return newInfo;
    }

    private ConcurrentMap<Integer, NestedFieldMappingInfo> buildNestedMappingInfo(Class<?> cls) {
        final ConcurrentMap<Integer, NestedFieldMappingInfo> mappingInfo = new ConcurrentHashMap<>();

        final ExtraMsgBody bodyProps = cls.getAnnotation(ExtraMsgBody.class);
        final JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);
        for (JavaBeanFieldMetadata metadata : beanMetadata.getFieldMetadataList()) {
            ExtraField.NestedFieldMapping nestedMetadata = metadata.getField().getAnnotation(ExtraField.NestedFieldMapping.class);
            if (nestedMetadata == null) {
                continue;
            }
            NestedFieldMappingInfo info = new NestedFieldMappingInfo();
            info.setMsgId(nestedMetadata.msgId());
            info.setDataType(nestedMetadata.dataType());
            info.setNestedExtraField(nestedMetadata.isNestedExtraField());
            info.setByteCountOfMsgId(bodyProps.byteCountOfMsgId());
            info.setByteCountOfContentLength(bodyProps.byteCountOfContentLength());
            info.setFieldMetadata(metadata);
            mappingInfo.put(nestedMetadata.msgId(), info);
        }
        return mappingInfo;
    }
}
