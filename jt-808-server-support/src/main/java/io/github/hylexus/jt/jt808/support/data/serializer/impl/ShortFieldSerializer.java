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
public class ShortFieldSerializer implements Jt808FieldSerializer<Short> {

    private static final Set<ResponseMsgConvertibleMetadata> SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808ResponseMsgDataType(short.class, MsgDataType.WORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(short.class, MsgDataType.BYTE),
            ConvertibleMetadata.forJt808ResponseMsgDataType(Short.class, MsgDataType.WORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(Short.class, MsgDataType.BYTE)
    );

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA;
    }

    @Override
    public void serialize(Short object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        switch (msgDataType) {
            case WORD:
                JtProtocolUtils.writeWord(byteBuf, object);
                break;
            case BYTE:
                byteBuf.writeByte(object);
                break;
            default: {
                throw new Jt808FieldSerializerException("Can not serialize Short/short as " + msgDataType);
            }
        }
    }
}
