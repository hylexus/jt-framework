package io.github.hylexus.jt.codec;

import io.github.hylexus.jt.annotation.msg.AdditionalField;
import io.github.hylexus.jt.data.msg.AdditionalFieldInfo;
import io.github.hylexus.jt.data.msg.AdditionalItemEntity;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.github.hylexus.jt.annotation.msg.AdditionalField.ROOT_GROUP_MSG_ID;

/**
 * @author hylexus
 * Created At 2019-09-28 11:41 下午
 */
@Slf4j
public class AdditionalFieldDecoder {

    private static final ConcurrentMap<Field, Map<Integer, AdditionalFieldInfo>> FIELD_INFO_CACHE = new ConcurrentHashMap<>();

    public void decodeAdditionalField(
            Object instance, byte[] bytes, int startIndex, int totalLength,
            JavaBeanFieldMetadata fieldMetadata) throws IllegalAccessException {

        AdditionalField annotation = fieldMetadata.getAnnotation(AdditionalField.class);

        Map<Integer, AdditionalFieldInfo> typeMapping = FIELD_INFO_CACHE.get(fieldMetadata.getField());
        if (typeMapping == null) {
            typeMapping = buildTypeMapping(annotation);
            FIELD_INFO_CACHE.put(fieldMetadata.getField(), typeMapping);
        }

        AdditionalFieldInfo rootGroupInfo = typeMapping.values().stream()
                .filter(e -> e.getGroupMsgId() == ROOT_GROUP_MSG_ID)
                .findFirst().orElseGet(() -> {
                    log.debug("Use default DEFAULT_ROOT_GROUP");
                    return AdditionalFieldInfo.DEFAULT_ROOT_GROUP;
                });

        List<AdditionalItemEntity> result = new ArrayList<>();
        parseExtraMsg(result, typeMapping, bytes, startIndex, totalLength,
                rootGroupInfo.getByteCountOfMsgId(), rootGroupInfo.getByteCountOfContentLength(), ROOT_GROUP_MSG_ID);

        setFieldValue(instance, fieldMetadata, result);

    }

    private Map<Integer, AdditionalFieldInfo> buildTypeMapping(AdditionalField annotation) {
        Map<Integer, AdditionalFieldInfo> typeMapping = new HashMap<>();
        // <groupMsgId, info>
        for (AdditionalField.MsgTypeMapping mapping : annotation.msgTypeMappings()) {
            AdditionalFieldInfo mappingInfo = new AdditionalFieldInfo();
            mappingInfo.setGroupMsgId(mapping.groupMsgId());
            mappingInfo.setNestedAdditionalField(mapping.isNestedAdditionalField());
            mappingInfo.setByteCountOfMsgId(mapping.byteCountOfMsgId());
            mappingInfo.setByteCountOfContentLength(mapping.byteCountOfContentLength());

            typeMapping.put(mapping.groupMsgId(), mappingInfo);
        }
        return typeMapping;
    }

    private void setFieldValue(Object instance, JavaBeanFieldMetadata fieldMetadata, List<AdditionalItemEntity> result)
            throws IllegalAccessException {
        fieldMetadata.getField().setAccessible(true);
        fieldMetadata.getField().set(instance, result);
    }

    private List<AdditionalItemEntity> parseExtraMsg(
            List<AdditionalItemEntity> result, Map<Integer, AdditionalFieldInfo> groupMapping,
            byte[] bytes, int startIndex, int totalLength, int byteCountOfMSgId, int byteCountOfContentLength, int groupMsgId) {

        int readerIndex = startIndex;
        while (readerIndex < totalLength) {
            final int msgId = IntBitOps.intFromBytes(bytes, readerIndex, byteCountOfMSgId);
            readerIndex += byteCountOfMSgId;

            final int contentLength = IntBitOps.intFromBytes(bytes, readerIndex, byteCountOfContentLength);
            readerIndex += byteCountOfContentLength;

            final byte[] contentBytes = Bytes.subSequence(bytes, readerIndex, contentLength);
            readerIndex += contentLength;

            AdditionalItemEntity additionalItemEntity = new AdditionalItemEntity();
            additionalItemEntity.setMsgId(msgId);
            additionalItemEntity.setGroupMsgId(groupMsgId);
            additionalItemEntity.setLength(contentLength);
            additionalItemEntity.setRawBytes(contentBytes);

            result.add(additionalItemEntity);


            AdditionalFieldInfo groupInfo;
            if (msgId != ROOT_GROUP_MSG_ID
                    && (groupInfo = groupMapping.get(msgId)) != null
                    && groupInfo.isNestedAdditionalField()) {

                return parseExtraMsg(result, groupMapping, contentBytes, 0, contentBytes.length,
                        groupInfo.getByteCountOfMsgId(),
                        groupInfo.getByteCountOfContentLength(), groupInfo.getGroupMsgId());
            }

        }

        return result;
    }

}
