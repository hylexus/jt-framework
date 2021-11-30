package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class LongFieldSerializer implements Jt808FieldSerializer<Long> {

    private static final Set<ResponseMsgConvertibleMetadata> SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA = Set.of(
            ConvertibleMetadata.forJt808ResponseMsgDataType(long.class, MsgDataType.DWORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(long.class, MsgDataType.WORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(long.class, MsgDataType.BYTE),
            ConvertibleMetadata.forJt808ResponseMsgDataType(Long.class, MsgDataType.DWORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(Long.class, MsgDataType.WORD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(Long.class, MsgDataType.BYTE)
    );

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA;
    }

    @Override
    public void serialize(Long object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        // TODO overflow --> mask
        // TODO NPE
        switch (msgDataType) {
            case DWORD:
                byteBuf.writeInt(object.intValue());
                break;
            case WORD:
                byteBuf.writeShort(object.shortValue());
                break;
            case BYTE:
                byteBuf.writeByte(object.byteValue());
                break;
            default: {
                throw new Jt808FieldSerializerException("Can not serialize Long/long as " + msgDataType);
            }
        }
    }
}