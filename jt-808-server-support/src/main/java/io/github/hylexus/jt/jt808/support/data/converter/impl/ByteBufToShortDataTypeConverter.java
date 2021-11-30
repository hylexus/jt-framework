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
 * Created At 2019-10-21 11:31 下午
 */
public class ByteBufToShortDataTypeConverter implements Jt808MsgDataTypeConverter<Short> {

    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Set.of(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, Short.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, short.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, Short.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, short.class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Short convert(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.WORD) {
//            return byteBuf.getShort(start);
            return byteBuf.readShort();
        } else if (msgDataType == MsgDataType.BYTE) {
//            return (short) byteBuf.getByte(start);
            return (short) byteBuf.readByte();
        }

        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to Short");
    }

}
