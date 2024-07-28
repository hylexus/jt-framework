package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.data.type.bytebuf.ByteBufContainer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Set;

public class ByteBufContainerFieldSerializer implements Jt808FieldSerializer<ByteBufContainer> {
    private static final Set<ResponseMsgConvertibleMetadata> SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteBufContainer.class, MsgDataType.BYTE),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteBufContainer.class, MsgDataType.BYTES),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteBufContainer.class, MsgDataType.WORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteBufContainer.class, MsgDataType.DWORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteBufContainer.class, MsgDataType.BCD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteBufContainer.class, MsgDataType.STRING)
    );

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA;
    }

    @Override
    public void serialize(ByteBufContainer object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        throw new NotImplementedException();
    }

    @Override
    public void serialize(ByteBufContainer object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException {
        final ByteBuf content = object.value();
        if (content == null) {
            return;
        }
        try {
            byteBuf.writeBytes(content, 0, object.length());
        } finally {
            content.release();
        }
    }
}

