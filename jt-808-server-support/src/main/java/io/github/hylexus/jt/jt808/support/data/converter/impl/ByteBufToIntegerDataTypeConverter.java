package io.github.hylexus.jt.jt808.support.data.converter.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class ByteBufToIntegerDataTypeConverter implements Jt808MsgDataTypeConverter<Integer> {

    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Set.of(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.DWORD, Integer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.DWORD, int.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, Integer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, int.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, Integer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, int.class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Integer convert(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.DWORD) {
//            return byteBuf.getInt(start);
            return byteBuf.readInt();
        } else if (msgDataType == MsgDataType.WORD) {
//            return (int) byteBuf.getShort(start);
            return (int) byteBuf.readShort();
        } else if (msgDataType == MsgDataType.BYTE) {
//            return (int) byteBuf.getByte(start);
            return (int) byteBuf.readByte();
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to Integer");
    }
}
