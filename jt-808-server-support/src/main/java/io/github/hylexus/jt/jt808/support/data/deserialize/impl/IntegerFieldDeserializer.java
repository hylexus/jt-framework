package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class IntegerFieldDeserializer implements Jt808FieldDeserializer<Integer> {

    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Jdk8Adapter.setOf(
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
    public Integer deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.DWORD) {
            return JtProtocolUtils.readDword(byteBuf);
        } else if (msgDataType == MsgDataType.WORD) {
            return JtProtocolUtils.readUnsignedWord(byteBuf);
        } else if (msgDataType == MsgDataType.BYTE) {
            return (int) byteBuf.readUnsignedByte();
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to Integer");
    }
}
