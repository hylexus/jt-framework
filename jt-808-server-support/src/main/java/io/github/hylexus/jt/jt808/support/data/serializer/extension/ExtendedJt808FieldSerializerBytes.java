package io.github.hylexus.jt.jt808.support.data.serializer.extension;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.type.bytebuf.ByteBufContainer;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

public class ExtendedJt808FieldSerializerBytes extends AbstractExtendedJt808FieldSerializer {

    @Override
    public void serialize(Object object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException {

        if (object == null) {
            return;
        }

        // 1. Object --> byte[]
        final ResponseFieldAlias.Bytes annotation = context.fieldMetadata().getAnnotation(ResponseFieldAlias.Bytes.class);
        final byte[] bytes;
        if (object instanceof String) {
            try {
                bytes = ((String) object).getBytes(annotation.charset());
            } catch (UnsupportedEncodingException e) {
                throw new Jt808FieldSerializerException(e);
            }
        } else if (object instanceof byte[]) {
            bytes = (byte[]) object;
        } else if (object instanceof ByteBufContainer) {
            final ByteBufContainer container = (ByteBufContainer) object;
            try {
                bytes = container.bytesValue();
            } finally {
                (container).release();
            }
        } else if (object instanceof ByteArrayContainer) {
            bytes = ((ByteArrayContainer) object).bytesValue();
        } else {
            throw new Jt808FieldSerializerException("Can not serializer field: " + context.fieldMetadata().getField());
        }

        // 2. byte[] --> padding
        if (bytes.length < annotation.paddingRight().minLength()) {
            JtProtocolUtils.paddingRight(byteBuf, bytes, annotation.paddingRight());
        } else if (bytes.length < annotation.paddingLeft().minLength()) {
            JtProtocolUtils.paddingLeft(byteBuf, bytes, annotation.paddingLeft());
        } else {
            byteBuf.writeBytes(bytes);
        }
    }
}
