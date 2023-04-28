package io.github.hylexus.jt.jt808.support.data.deserialize.impl;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractJt808FieldDeserializer<T> implements Jt808FieldDeserializer<T> {

    protected final Set<RequestMsgConvertibleMetadata> convertibleMetadata;

    public AbstractJt808FieldDeserializer(Set<RequestMsgConvertibleMetadata> convertibleMetadata) {
        this.convertibleMetadata = Collections.unmodifiableSet(convertibleMetadata);
    }

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return this.convertibleMetadata;
    }

    @Override
    public T deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        throw new NotImplementedException();
    }

    @Override
    public abstract T deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context);

}
