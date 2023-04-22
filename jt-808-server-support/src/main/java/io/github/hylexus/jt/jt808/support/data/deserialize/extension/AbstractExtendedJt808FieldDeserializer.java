package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractExtendedJt808FieldDeserializer<T> implements Jt808FieldDeserializer<T> {

    @Override
    public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
        return Collections.emptySet();
    }

    @Override
    public T deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length) {
        throw new NotImplementedException();
    }
}
