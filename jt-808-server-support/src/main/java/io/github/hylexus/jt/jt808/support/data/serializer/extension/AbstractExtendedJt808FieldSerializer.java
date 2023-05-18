package io.github.hylexus.jt.jt808.support.data.serializer.extension;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collections;
import java.util.Set;

public class AbstractExtendedJt808FieldSerializer implements Jt808FieldSerializer<Object> {
    @Override
    public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
        return Collections.emptySet();
    }

    @Override
    public void serialize(Object object, MsgDataType msgDataType, ByteBuf byteBuf) throws Jt808FieldSerializerException {
        throw new NotImplementedException();
    }
}
