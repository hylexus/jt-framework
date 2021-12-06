package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class StringFieldSerializer implements Jt808FieldSerializer<String> {

    private static final Set<ResponseMsgConvertibleMetadata> SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA = Set.of(
            ConvertibleMetadata.forJt808ResponseMsgDataType(String.class, MsgDataType.BCD),
            ConvertibleMetadata.forJt808ResponseMsgDataType(String.class, MsgDataType.STRING)
    );

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return SUPPORTED_RESPONSE_MSG_CONVERTIBLE_METADATA;
    }

    @Override
    public void serialize(String object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        // TODO NPE
        switch (msgDataType) {
            case STRING:
                JtProtocolUtils.writeString(byteBuf, object);
                break;
            case BCD:
                JtProtocolUtils.writeBcd(byteBuf, object);
                break;
            default: {
                throw new Jt808FieldSerializerException("Can not serialize String as " + msgDataType);
            }
        }
    }
}