package io.github.hylexus.jt.jt808.support.data.type;

import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
//@Deprecated(since = "2.1.1")
@Deprecated
public class ByteWrapper implements BytesValueWrapper<Byte> {
    private Byte value;

    public ByteWrapper() {
    }

    public ByteWrapper(Byte value) {
        this.value = value;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeByte(value);
    }

    @Override
    public Byte read(ByteBuf byteBuf, int offset, int length) {
        value = byteBuf.readByte();
        return value;
    }

    @Override
    public Byte value() {
        return value;
    }
}
