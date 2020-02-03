package io.github.hylexus.jt.codec.encode;

import io.github.hylexus.jt.annotation.Transient;
import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.resp.BytesValueWrapper;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * @author hylexus
 * Created At 2020-02-02 3:30 下午
 */
@Slf4j
public class CommonFieldEncoder {

    public List<byte[]> encodeMsgBodyRecursively(Object instance, List<byte[]> byteList) throws IllegalAccessException, InstantiationException {
        Class<?> cls = instance.getClass();
        JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {
            if (fieldMetadata.isAnnotationPresent(Transient.class)) {
                continue;
            }
            // 1. 只处理用 @CommandField 标记过的属性
            if (fieldMetadata.isAnnotationPresent(CommandField.class)) {
                final Object value = fieldMetadata.getFieldValue(instance, true);

                // 1.1 处理BytesValueWrapper
                if (value instanceof BytesValueWrapper) {
                    byteList.add(((BytesValueWrapper<?>) value).getAsBytes());
                    continue;
                }

                // 2.2 处理Collection
                if (value instanceof Collection) {
                    for (Object item : ((Collection<?>) value)) {
                        encodeMsgBodyRecursively(item, byteList);
                    }
                    continue;
                }

                // 2.3 基本类型的处理
                final CommandField annotation = fieldMetadata.getAnnotation(CommandField.class);
                final MsgDataType targetMsgDataType = annotation.targetMsgDataType();
                byte[] result = processSpecificDataType(targetMsgDataType, value);
                if (result != null) {
                    byteList.add(result);
                    continue;
                }

                // 2.4 其他无法处理的属性 --> 忽略
                log.debug("Can not encode field : {}, but annotation @{} found", fieldMetadata.getField(), CommandField.class.getSimpleName());
            } else {
                // 2. 没有用 @CommandField 标记过的属性属性跳过
                log.debug("No annotation @{} found on field [{}], encoding skipped.", CommandField.class.getSimpleName(), fieldMetadata.getField());
            }
        }
        return byteList;
    }

    private byte[] processSpecificDataType(MsgDataType targetMsgDataType, Object value) {
        final Class<?> targetClass = value.getClass();
        if (targetClass == int.class || targetClass == Integer.class) {
            switch (targetMsgDataType) {
                case BYTE:
                    return IntBitOps.intTo1Byte((Integer) value);
                case WORD:
                    return IntBitOps.intTo2Bytes((Integer) value);
                case DWORD:
                    return IntBitOps.intTo4Bytes((Integer) value);
                default: {
                    log.debug("Can not encode value {} to {}", value, targetMsgDataType);
                    return null;
                }
            }
        }

        if (targetClass == byte.class || targetClass == Byte.class) {
            return new byte[(Byte) value];
        }

        log.debug("Can not encode value {} to {}", value, targetMsgDataType);
        return null;
    }

}
