package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class ByteBufFieldSerializer implements Jt808FieldSerializer<ByteBuf> {

    private static final Set<ResponseMsgConvertibleMetadata> SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808ResponseMsgDataType(ByteBuf.class, MsgDataType.BYTES)
    );

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA;
    }

    @Override
    public void serialize(ByteBuf object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        if (msgDataType == MsgDataType.BYTES) {
            JtProtocolUtils.writeByteBuf(byteBuf, object, true);
            return;
        }
        throw new Jt808FieldSerializerException("Can not serialize [ByteBuf] as " + msgDataType);
    }
}
