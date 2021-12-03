package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 * Created At 2021-02-14 18:31 下午
 */
public class LongFieldDeserializer implements Jt808FieldDeserializer<Long> {

    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Set.of(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.DWORD, Long.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.DWORD, long.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, Long.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, long.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, Long.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, long.class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Long deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.DWORD) {
            return byteBuf.readLong();
        } else if (msgDataType == MsgDataType.WORD) {
            return (long) byteBuf.readShort();
        } else if (msgDataType == MsgDataType.BYTE) {
            return (long) byteBuf.readByte();
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to Long");
    }

}
