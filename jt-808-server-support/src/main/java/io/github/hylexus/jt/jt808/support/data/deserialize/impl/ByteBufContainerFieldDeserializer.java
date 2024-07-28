package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.type.bytebuf.ByteBufContainer;
import io.github.hylexus.jt.jt808.support.data.type.bytebuf.DefaultByteBufContainer;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Set;

public class ByteBufContainerFieldDeserializer implements Jt808FieldDeserializer<ByteBufContainer> {

    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTES, ByteBufContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, ByteBufContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.STRING, ByteBufContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BCD, ByteBufContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, ByteBufContainer.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.DWORD, ByteBufContainer.class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public ByteBufContainer deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        throw new NotImplementedException();
    }

    /**
     * 请求消息中的 {@link ByteBufContainer} 实例会随着 {@link Jt808Request#release()} 一起释放。
     *
     * @see Jt808Request#release()
     */
    @Override
    public ByteBufContainer deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        // 这里没有 retain: 随着 Request.release 一起释放掉
        final ByteBuf content = byteBuf.readSlice(length);
        return new DefaultByteBufContainer(content);
    }
}
