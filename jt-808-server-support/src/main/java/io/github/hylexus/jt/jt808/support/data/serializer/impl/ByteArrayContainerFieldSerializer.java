package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

public class ByteArrayContainerFieldSerializer implements Jt808FieldSerializer<ByteArrayContainer> {
    private static final Set<ResponseMsgConvertibleMetadata> SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteArrayContainer.class, MsgDataType.BYTE),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteArrayContainer.class, MsgDataType.BYTES),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteArrayContainer.class, MsgDataType.WORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteArrayContainer.class, MsgDataType.DWORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteArrayContainer.class, MsgDataType.BCD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteArrayContainer.class, MsgDataType.STRING)
    );

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA;
    }

    @Override
    public void serialize(ByteArrayContainer object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        byteBuf.writeBytes(object.value(), 0, object.length());
    }
}
