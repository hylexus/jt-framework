package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class StringFieldSerializer extends AbstractJt808FieldSerializer<String> {

    public StringFieldSerializer() {
        super(Jdk8Adapter.setOf(
                ConvertibleMetadata.forJt808ResponseMsgDataType(String.class, MsgDataType.BCD),
                ConvertibleMetadata.forJt808ResponseMsgDataType(String.class, MsgDataType.STRING),
                ConvertibleMetadata.forJt808ResponseMsgDataType(String.class, MsgDataType.BYTES))
        );
    }

    @Override
    public void serialize(String object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException {
        if (object == null) {
            return;
        }

        switch (msgDataType) {
            case STRING:
            case BYTES: {
                final ResponseField annotation = context.fieldMetadata().getAnnotation(ResponseField.class);
                final byte[] bytes = object.getBytes(context.fieldMetadata().getFieldCharset());
                JtProtocolUtils.writeBytesWithPadding(byteBuf, bytes, annotation);
                break;
            }
            case BCD: {
                JtProtocolUtils.writeBcd(byteBuf, object);
                break;
            }
            default: {
                throw new Jt808FieldSerializerException("Can not serialize String as " + msgDataType);
            }
        }
    }
}
