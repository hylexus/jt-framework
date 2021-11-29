package io.github.hylexus.jt.jt808.support.data.converter;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public interface Jt808MsgDataTypeConverter<T> {

    Set<ConvertibleMetadata> getConvertibleTypes();

    T convert(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length);

}
