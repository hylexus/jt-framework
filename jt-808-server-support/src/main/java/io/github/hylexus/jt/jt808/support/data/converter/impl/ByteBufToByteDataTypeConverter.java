package io.github.hylexus.jt.jt808.support.data.converter.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class ByteBufToByteDataTypeConverter implements Jt808MsgDataTypeConverter<Byte> {

    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET = Set.of(
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, Byte.class),
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, byte.class)
    );

    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Byte convert(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.BYTE) {
//            return byteBuf.getByte(start);
            return byteBuf.readByte();
        }

        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to Byte");
    }
}
