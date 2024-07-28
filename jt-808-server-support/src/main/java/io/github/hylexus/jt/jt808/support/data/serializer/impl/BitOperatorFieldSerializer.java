package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.BitOperator;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

public class BitOperatorFieldSerializer extends AbstractJt808FieldSerializer<BitOperator> {

    public BitOperatorFieldSerializer() {
        super(Jdk8Adapter.setOf(
                ConvertibleMetadata.forJt808ResponseMsgDataType(BitOperator.class, MsgDataType.DWORD),
                ConvertibleMetadata.forJt808ResponseMsgDataType(BitOperator.class, MsgDataType.WORD),
                ConvertibleMetadata.forJt808ResponseMsgDataType(BitOperator.class, MsgDataType.BYTE)
        ));
    }

    @Override
    public void serialize(BitOperator object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException {
        switch (msgDataType) {
            case BYTE: {
                JtProtocolUtils.writeByte(byteBuf, object.byteValue());
                break;
            }
            case WORD: {
                JtProtocolUtils.writeWord(byteBuf, object.wordValue());
                break;
            }
            case DWORD: {
                JtProtocolUtils.writeDword(byteBuf, object.dwordValue());
                break;
            }
            default: {
                throw new Jt808FieldSerializerException("Can not serialize BitOperator as " + msgDataType);
            }
        }
    }
}
