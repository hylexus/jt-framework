package io.github.hylexus.jt.jt808.support.data.type;

import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class ByteBufWrapper implements BytesValueWrapper<ByteBuf> {
    private ByteBuf value;

    public ByteBufWrapper() {
    }

    public ByteBufWrapper(ByteBuf value) {
        this.value = value;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.value);
    }

    @Override
    public ByteBuf read(ByteBuf byteBuf, int offset, int length) {
        value = byteBuf.slice(offset, length);
        return value;
    }

    @Override
    public ByteBuf value() {
        return value;
    }
}
