package io.github.hylexus.jt.jt808.support.data.converter.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class ByteBufToByteArrayDataTypeConverter implements Jt808MsgDataTypeConverter<byte[]> {
    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Set.of(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTES, byte[].class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTES, Byte[].class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public byte[] convert(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.BYTES) {
//            return JtProtocolUtils.getBytes(byteBuf, start, length);
            return JtProtocolUtils.readBytes(byteBuf, start, length);
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to byte[]");
    }

}
