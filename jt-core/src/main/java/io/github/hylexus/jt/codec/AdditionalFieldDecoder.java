package io.github.hylexus.jt.codec;

import io.github.hylexus.jt.annotation.msg.AdditionalField;
import io.github.hylexus.jt.data.msg.AdditionalInfo;
import io.github.hylexus.jt.data.msg.BuiltinAdditionalInfo;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hylexus
 * Created At 2019-09-28 11:41 下午
 */
public class AdditionalFieldDecoder {

    public void decodeAdditionalField(
            Class<?> cls, Object instance, byte[] bytes, int currentOffset,
            JavaBeanFieldMetadata fieldMetadata, int startIndex, int totalLength) {

        AdditionalField annotation = fieldMetadata.getAnnotation(AdditionalField.class);
        if (annotation == null) {
            return;
        }
        if (fieldMetadata.getFieldType() == List.class) {
            // TODO
            return;
        }
        List<Class<?>> genericType = fieldMetadata.getGenericType();
        Class<?> keyClass = genericType.get(0);
        Class<?> valueClass = genericType.get(1);

        System.out.println(keyClass);
        System.out.println(valueClass);
        Class<? extends AdditionalInfo> entityClass = annotation.entityClass();
        System.out.println(entityClass);
        System.out.println(HexStringUtils.bytes2HexString(bytes));
        System.out.println(HexStringUtils.bytes2HexString(Bytes.subSequence(bytes, startIndex, totalLength)));

        Map<Integer, AdditionalInfo> map = new LinkedHashMap<>();
        int readerIndex = startIndex;
        while (readerIndex < totalLength) {
            final int msgId = IntBitOps.intFromBytes(bytes, readerIndex, 1);
            readerIndex += 1;

            int contentLength = IntBitOps.intFromBytes(bytes, readerIndex, 1);
            readerIndex += 1;

            byte[] content = Bytes.subSequence(bytes, readerIndex, contentLength);
            readerIndex += contentLength;

            BuiltinAdditionalInfo item = new BuiltinAdditionalInfo(msgId, contentLength, content);
            map.put(msgId, item);
        }

        map.forEach((k, v) -> {
            String key = HexStringUtils.int2HexString(k, 2, true);
            System.out.println(key + " -- " + v.getContentLength() + " -- " + v.getContent().length);
        });
    }
}
