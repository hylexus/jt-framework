package io.github.hylexus.jt.jt808.support.data.serializer;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;

import java.util.Set;

public interface Jt808FieldSerializer<T> {

    Set<ResponseMsgConvertibleMetadata> getSupportedTypes();

    void serialize(T object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException;

}