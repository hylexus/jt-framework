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
 */
public class BcdFieldDeserializer implements Jt808FieldDeserializer<String> {
    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Jdk8Adapter.setOf(
            ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BCD, String.class)
    );

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public String deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.BCD) {
            return JtProtocolUtils.readBcd(byteBuf, length);
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to BCD");
    }

}
