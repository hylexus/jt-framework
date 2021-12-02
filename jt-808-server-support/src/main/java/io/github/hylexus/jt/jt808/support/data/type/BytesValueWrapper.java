package io.github.hylexus.jt.jt808.support.data.type;

import io.netty.buffer.ByteBuf;

/**
 * Created At 2019-10-17 10:46 下午
 *
 * @author hylexus
 */
public interface BytesValueWrapper<T> {

    void write(ByteBuf byteBuf);

    T read(ByteBuf byteBuf, int offset, int length);

    T value();
}
