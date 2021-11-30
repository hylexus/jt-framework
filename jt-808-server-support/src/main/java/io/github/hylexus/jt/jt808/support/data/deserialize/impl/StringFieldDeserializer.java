package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author hylexus
 */
public class StringFieldDeserializer implements Jt808FieldDeserializer<String> {
    private static final Set<RequestMsgConvertibleMetadata> CONVERTIBLE_METADATA_SET = Set.of(ConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.STRING, String.class));
    private final Charset charset;

    public StringFieldDeserializer() {
        this(JtProtocolConstant.JT_808_STRING_ENCODING);
    }

    public StringFieldDeserializer(Charset charset) {
        this.charset = charset;
    }

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public String deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        if (msgDataType == MsgDataType.STRING) {
//            return JtProtocolUtils.getString(byteBuf, start, length, charset);
            return JtProtocolUtils.readString(byteBuf, start, length, charset);
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to String");
    }
}
