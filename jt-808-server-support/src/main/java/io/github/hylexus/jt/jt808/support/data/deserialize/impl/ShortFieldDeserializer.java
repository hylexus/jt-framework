package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-21 11:31 下午
 */
public class ShortFieldDeserializer implements Jt808FieldDeserializer<Short> {

    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, Short.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.WORD, short.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, Short.class),
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTE, short.class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Short deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.WORD) {
            return JtProtocolUtils.readWord(byteBuf);
        } else if (msgDataType == MsgDataType.BYTE) {
            return byteBuf.readUnsignedByte();
        }

        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to Short");
    }

}
