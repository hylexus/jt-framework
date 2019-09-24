package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.annotation.Jt808Field;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.DataTypeConverter;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgMetadataAware;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static io.github.hylexus.jt.config.JtProtocolConstant.JT_808_STRING_ENCODING;
import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
@Slf4j
public class Decoder {

    private Map<Class<? extends DataTypeConverter>, DataTypeConverter> converterMapping = new HashMap<>();

    public RequestMsgMetadata parseMsgMetadata(byte[] bytes) {
        final RequestMsgMetadata ret = new RequestMsgMetadata();

        // 1. 消息头 16byte 或 12byte
        final RequestMsgHeader msgHeader = this.parseMsgHeaderFromBytes(bytes);
        ret.setHeader(msgHeader);

        // 2. 消息体
        // 有子包信息,消息体起始字节后移四个字节:消息包总数(word(16))+包序号(word(16))
        final int msgBodyByteStartIndex = msgHeader.isHasSubPackage() ? 16 : 12;
        ret.setBodyBytes(Bytes.subSequence(bytes, msgBodyByteStartIndex, msgHeader.getMsgBodyLength()));

        // 3. 去掉分隔符之后，最后一位就是校验码
        final byte checkSumInPkg = bytes[bytes.length - 1];
        ret.setCheckSum(checkSumInPkg);
        validateCheckSum(bytes, msgHeader, checkSumInPkg);
        return ret;
    }

    private void validateCheckSum(byte[] bytes, RequestMsgHeader msgHeader, byte checkSumInPkg) {
        final int calculatedCheckSum = ProtocolUtils.calculateCheckSum4Jt808(bytes, 0, bytes.length - 1);
        if (checkSumInPkg != calculatedCheckSum) {
            log.warn("检验码不一致,msgId:{},expected : {},calculated : {}", msgHeader.getMsgId(), checkSumInPkg, calculatedCheckSum);
        }
    }

    private RequestMsgHeader parseMsgHeaderFromBytes(byte[] bytes) {
        final RequestMsgHeader header = new RequestMsgHeader();

        // 1. byte[0-1]   消息ID word(16)
        header.setMsgId(intFromBytes(bytes, 0, 2));

        // 2. byte[2-3]   消息体属性 word(16)
        final int bodyProps = intFromBytes(bytes, 2, 2);
        header.setMsgBodyPropsField(bodyProps);
        // [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
        header.setMsgBodyLength(bodyProps & 0x3ff);
        // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
        header.setEncryptionType((bodyProps & 0x1c00) >> 10);
        // [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
        header.setHasSubPackage(((bodyProps & 0x2000) >> 13) == 1);
        // [14-15] 1100,0000,0000,0000(C000)(保留位)
        header.setReservedBit(((bodyProps & 0xc000) >> 14));

        // 3. byte[4-9]   终端手机号或设备ID bcd[6]
        header.setTerminalId(BcdOps.bytes2BcdString(bytes, 4, 6));

        // 4. byte[10-11]     消息流水号 word(16)
        header.setFlowId(intFromBytes(bytes, 10, 2));

        // 5. byte[12-15]     消息包封装项
        if (header.isHasSubPackage()) {
            // byte[0-1]   消息包总数(word(16))
            header.setTotalSubPackage(intFromBytes(bytes, 12, 2));
            // byte[2-3]   包序号(word(16))
            header.setSubPackageSeq(intFromBytes(bytes, 14, 2));
        }
        return header;
    }

    public <T> T decodeRequestMsgBody(Class<T> cls, byte[] bytes, RequestMsgMetadata metadata)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {

        T instance = cls.newInstance();
        processAwareMethod(cls, instance, metadata);

        Field[] fields = cls.getDeclaredFields();
        int currentOffset = 0;
        for (Field field : fields) {
            final Jt808Field annotation = field.getAnnotation(Jt808Field.class);
            if (annotation == null) {
                continue;
            }

            final MsgDataType dataType = annotation.dataType();
            final Class<?> fieldType = field.getType();
            final int startIndex = annotation.startIndex();

            int length = getFieldLength(cls, instance, annotation, dataType);

            final Class<? extends DataTypeConverter> converterClass = annotation.customerDataTypeConverterClass();
            // 使用用户自定义的属性转换器
            if (converterClass != DataTypeConverter.NoOpsConverter.class) {
                populateFieldByCustomerConverter(bytes, instance, field, converterClass, startIndex, length);
            } else {
                // 默认的属性转换策略
                if (dataType.getExpectedTargetClassType().contains(fieldType)) {
                    populateField(currentOffset, bytes, instance, field, dataType, startIndex, length);
                } else {
                    // 没有配置【自定义属性转换器】&& 是不支持的目标类型
                    throw new IllegalArgumentException("No customerDataTypeConverterClass found, Unsupported expectedTargetClassType "
                            + fieldType + " for field " + field);
                }
            }

            currentOffset += length;
        }
        return instance;
    }

    private <T> void processAwareMethod(Class<T> cls, Object instance, RequestMsgMetadata metadata) {
        if (instance instanceof RequestMsgHeaderAware) {
            ((RequestMsgHeaderAware) instance).setRequestMsgHeader(metadata.getHeader());
        }

        if (instance instanceof RequestMsgMetadataAware) {
            ((RequestMsgMetadataAware) instance).setRequestMsgMetadata(metadata);
        }
    }

    private <T> Integer getFieldLength(Class<T> cls, T instance, Jt808Field annotation, MsgDataType dataType) throws IllegalAccessException,
            InvocationTargetException {

        int length = dataType.getByteCount() == 0
                ? annotation.length()
                : dataType.getByteCount();

        if (length <= 0) {
            if (dataType == MsgDataType.COLLECTION) {
                return JtProtocolConstant.FIELD_LENGTH_UNKNOWN;
            }

            Method method = ReflectionUtils.findMethod(cls, annotation.byteCountMethod());
            if (method == null) {
                throw new NoSuchMethodError("No byteCountMethod() method found : " + annotation.byteCountMethod());
            }

            return (Integer) method.invoke(instance);
        }
        return length;
    }

    private void populateField(int currentOffset, byte[] bytes, Object instance, Field field, MsgDataType dataType, int startIndex, int length)
            throws IllegalAccessException {
        final Object value;
        switch (dataType) {
            case WORD: {
                value = (short) intFromBytes(bytes, startIndex, length);
                break;
            }
            case DWORD: {
                value = intFromBytes(bytes, startIndex, length);
                break;
            }
            case BYTE: {
                value = bytes[startIndex];
                break;
            }
            case BYTES: {
                value = Bytes.subSequence(bytes, startIndex, length);
                break;
            }
            case BCD: {
                value = BcdOps.bytes2BcdString(bytes, startIndex, length);
                break;
            }
            case STRING: {
                value = new String(Bytes.subSequence(bytes, startIndex, length), JT_808_STRING_ENCODING);
                break;
            }
            case COLLECTION: {
                // TODO
                Class<?> c = null;
                try {
                    c = Class.forName("io.github.hylexus.jt808.server.msg.req.LocationUploadMsgBody$ExtraInfo");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    Object o = decodeRequestMsgBody(c, bytes, null);
                    System.out.println(o);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                value = null;
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + dataType);
        }
        setFieldValue(instance, field, value);
    }

    private void populateFieldByCustomerConverter(
            byte[] bytes, Object instance, Field field,
            Class<? extends DataTypeConverter> converterClass,
            int start, int byteCount) throws InstantiationException, IllegalAccessException {

        DataTypeConverter converter = getDataTypeConverter(converterClass);
        Object value = converter.convert(bytes, Bytes.subSequence(bytes, start, byteCount));
        setFieldValue(instance, field, value);
    }

    private DataTypeConverter getDataTypeConverter(
            Class<? extends DataTypeConverter> converterClass) throws InstantiationException, IllegalAccessException {

        DataTypeConverter converter = converterMapping.get(converterClass);
        if (converter == null) {
            converter = converterClass.newInstance();
            converterMapping.put(converterClass, converter);
        }
        return converter;
    }

    private void setFieldValue(Object instance, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }
}
