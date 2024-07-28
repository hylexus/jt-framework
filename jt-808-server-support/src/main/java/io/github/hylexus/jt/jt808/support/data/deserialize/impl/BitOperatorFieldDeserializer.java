package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.BitOperator;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

public class BitOperatorFieldDeserializer extends AbstractJt808FieldDeserializer<BitOperator> {

    public BitOperatorFieldDeserializer() {
        super(Jdk8Adapter.setOf(
                ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.DWORD, BitOperator.class),
                ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, BitOperator.class),
                ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, BitOperator.class)
        ));
    }

    @Override
    public BitOperator deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final long value;
        switch (msgDataType) {
            case DWORD: {
                value = JtProtocolUtils.readUnsignedDword(byteBuf);
                break;
            }
            case WORD: {
                value = JtProtocolUtils.readUnsignedWord(byteBuf);
                break;
            }
            case BYTE: {
                value = JtProtocolUtils.readUnsignedByte(byteBuf);
                break;
            }
            default: {
                throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to BitOperator");
            }
        }

        return BitOperator.mutable(value);
    }

}
