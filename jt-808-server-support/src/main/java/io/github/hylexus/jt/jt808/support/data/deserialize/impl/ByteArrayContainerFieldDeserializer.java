package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

public class ByteArrayContainerFieldDeserializer implements Jt808FieldDeserializer<ByteArrayContainer> {
    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTES, ByteArrayContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, ByteArrayContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.STRING, ByteArrayContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BCD, ByteArrayContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, ByteArrayContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.DWORD, ByteArrayContainer.class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public ByteArrayContainer deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        // 这里必须先读取
        final byte[] bytes = JtProtocolUtils.readBytes(byteBuf, length);
        return () -> bytes;
    }
}
