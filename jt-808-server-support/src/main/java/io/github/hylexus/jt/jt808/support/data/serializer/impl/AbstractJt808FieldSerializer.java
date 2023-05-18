package io.github.hylexus.jt.jt808.support.data.serializer.impl;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractJt808FieldSerializer<T> implements Jt808FieldSerializer<T> {
    protected final Set<ResponseMsgConvertibleMetadata> convertibleMetadata;

    public AbstractJt808FieldSerializer(Set<ResponseMsgConvertibleMetadata> convertibleMetadata) {
        this.convertibleMetadata = Collections.unmodifiableSet(convertibleMetadata);
    }

    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return this.convertibleMetadata;
    }

    @Override
    public void serialize(T object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        throw new NotImplementedException();
    }

    @Override
    public abstract void serialize(T object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException;
}
