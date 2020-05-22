package io.github.hylexus.jt.codec.encode;

import io.github.hylexus.jt.annotation.Transient;
import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.resp.entity.RespMsgFieldConverter;
import io.github.hylexus.jt.data.resp.BytesValueWrapper;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.oaks.utils.BcdOps;
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

            // 1. 没有用 @CommandField 标记过的属性属性跳过
            if (!fieldMetadata.isAnnotationPresent(CommandField.class)) {
                log.debug("No annotation @{} found on field [{}], encoding skipped.", CommandField.class.getSimpleName(), fieldMetadata.getField());
                continue;
            }

            // 2. 处理用 @CommandField 标记过的属性
            final Object value = fieldMetadata.getFieldValue(instance, true);

            // 2.1 处理BytesValueWrapper
            if (value instanceof BytesValueWrapper) {
                byteList.add(((BytesValueWrapper<?>) value).getAsBytes());
                continue;
            }

            // 2.2 处理基本类型
            final CommandField annotation = fieldMetadata.getAnnotation(CommandField.class);
            this.doEncodeField(byteList, value, fieldMetadata, annotation);

        }
        return byteList;
    }

    private void doEncodeField(List<byte[]> byteList, Object value, JavaBeanFieldMetadata fieldMetadata, CommandField annotation) throws IllegalAccessException,
            InstantiationException {
        // 1. 自定义转换器
        if (annotation.customerDataTypeConverterClass() != RespMsgFieldConverter.NoOpsConverter.class) {
            byte[] result = encodeByCustomerConverter(value, annotation);
            byteList.add(result);
            return;
        }

        final MsgDataType targetMsgDataType = annotation.targetMsgDataType();
        if (!isSupportedType(fieldMetadata, targetMsgDataType, annotation)) {
            throw new IllegalArgumentException("No customerDataTypeConverterClass found, Unsupported expectedTargetClassType "
                    + fieldMetadata.getFieldType() + " for field " + fieldMetadata.getField());
        }

        switch (targetMsgDataType) {
            case BYTE: {
                byteList.add(IntBitOps.intTo1Byte(((Number) (value)).intValue()));
                break;
            }
            case WORD: {
                byteList.add(IntBitOps.intTo2Bytes(((Number) (value)).intValue()));
                break;
            }
            case DWORD: {
                byteList.add(IntBitOps.intTo4Bytes(((Number) (value)).intValue()));
                break;
            }
            case BYTES: {
                byteList.add((byte[]) value);
                break;
            }
            case BCD: {
                assert value.getClass() == String.class;
                byteList.add(BcdOps.bcdString2bytes((String) value));
                break;
            }
            case STRING: {
                byte[] result = ((String) value).getBytes(JtProtocolConstant.JT_808_STRING_ENCODING);
                byteList.add(result);
                break;
            }
            default: {
                if (value instanceof Collection) {
                    for (Object o : (Collection<?>) value) {
                        this.encodeMsgBodyRecursively(o, byteList);
                    }
                } else if (annotation.isNestedCommandField()) {
                    this.encodeMsgBodyRecursively(value, byteList);
                }
            }
        }
    }

    private boolean isSupportedType(JavaBeanFieldMetadata fieldMetadata, MsgDataType targetMsgDataType, CommandField annotation) {
        if (targetMsgDataType == MsgDataType.UNKNOWN) {
            return annotation.isNestedCommandField() || Collection.class.isAssignableFrom(fieldMetadata.getFieldType());
        }

        return targetMsgDataType.getExpectedTargetClassType().contains(fieldMetadata.getFieldType());
    }

    private byte[] encodeByCustomerConverter(Object value, CommandField annotation) throws InstantiationException, IllegalAccessException {
        // TODO cache
        final RespMsgFieldConverter converter = annotation.customerDataTypeConverterClass().newInstance();
        final byte[] bytes = converter.convert(value);
        return bytes;
    }

}
