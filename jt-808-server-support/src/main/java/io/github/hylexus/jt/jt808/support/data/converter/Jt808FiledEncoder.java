package io.github.hylexus.jt.jt808.support.data.converter;

import io.github.hylexus.jt.jt808.support.data.ConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public interface Jt808FiledEncoder<T> {

    Set<ConvertibleMetadata> getConvertibleTypes();

    T convert(Class<?> cls, MsgDataType msgDataType, ByteBuf byteBuf);

}
