package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @author hylexus
 */
public class StringFieldDeserializer extends AbstractJt808FieldDeserializer<String> {

    public StringFieldDeserializer() {
        super(Jdk8Adapter.setOf(
                ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.STRING, String.class),
                ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTES, String.class)
        ));
    }

    @Override
    public String deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final Charset charset = context.fieldMetadata().getFieldCharset();
        return JtProtocolUtils.readString(byteBuf, length, charset);
    }
}
