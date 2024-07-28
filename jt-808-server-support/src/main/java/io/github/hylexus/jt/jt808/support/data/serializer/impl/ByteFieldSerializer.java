package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class ByteFieldSerializer implements Jt808FieldSerializer<Byte> {

    private static final Set<ResponseMsgConvertibleMetadata> SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808ResponseMsgDataType(byte.class, MsgDataType.BYTE),
            ConvertibleMetadata.forJt808ResponseMsgDataType(Byte.class, MsgDataType.BYTE)
    );

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA;
    }

    @Override
    public void serialize(Byte object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        if (msgDataType == MsgDataType.BYTE) {
            byteBuf.writeByte(object);
            return;
        }
        throw new Jt808FieldSerializerException("Can not serialize Byte/byte as " + msgDataType);
    }
}
