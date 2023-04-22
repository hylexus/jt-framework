package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @see RequestFieldAlias.String
 * @see io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2011#getTime()
 * @see io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2013#getTime()
 * @see io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2019#getTime()
 */
public class ExtendedJt808FieldDeserializerString extends AbstractExtendedJt808FieldDeserializer<String> {

    @Override
    public String deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final String charset = context.fieldMetadata().getAnnotation(RequestFieldAlias.String.class).charset();
        return JtProtocolUtils.readString(byteBuf, length, Charset.forName(charset));
    }
}
