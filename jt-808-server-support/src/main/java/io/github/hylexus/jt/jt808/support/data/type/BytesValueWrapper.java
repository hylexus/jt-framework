package io.github.hylexus.jt.jt808.support.data.type;

import io.netty.buffer.ByteBuf;

/**
 * Created At 2019-10-17 10:46 下午
 *
 * @author hylexus
 * @see io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer
 * @see io.github.hylexus.jt.jt808.support.data.type.bytebuf.ByteBufContainer
 * @deprecated 使用 {@link io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer ByteArrayContainer} 或
 * {@link io.github.hylexus.jt.jt808.support.data.type.bytebuf.ByteBufContainer ByteBufContainer} 代替.
 */
//@Deprecated(since = "2.1.1")
@Deprecated
public interface BytesValueWrapper<T> {

    void write(ByteBuf byteBuf);

    T read(ByteBuf byteBuf, int offset, int length);

    T value();
}
