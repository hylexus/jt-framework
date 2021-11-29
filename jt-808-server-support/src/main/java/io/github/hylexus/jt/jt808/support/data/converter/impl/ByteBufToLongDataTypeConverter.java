package io.github.hylexus.jt.jt808.support.data.converter.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 * Created At 2021-02-14 18:31 下午
 */
public class ByteBufToLongDataTypeConverter implements Jt808MsgDataTypeConverter<Long> {

    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET = Set.of(
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.DWORD, Long.class),
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.DWORD, long.class),
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, Long.class),
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, long.class),
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, Long.class),
            ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, long.class)
    );

    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Long convert(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.DWORD) {
//            return byteBuf.getLong(start);
            return byteBuf.readLong();
        } else if (msgDataType == MsgDataType.WORD) {
//            return (long) byteBuf.getShort(start);
            return (long) byteBuf.readShort();
        } else if (msgDataType == MsgDataType.BYTE) {
//            return (long) byteBuf.getByte(start);
            return (long) byteBuf.readByte();
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to Long");
    }

}
