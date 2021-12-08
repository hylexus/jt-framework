package io.github.hylexus.jt.jt808.support.data.type;

import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class ByteArrayWrapper implements BytesValueWrapper<byte[]> {
    private byte[] value;

    public ByteArrayWrapper() {
    }

    public ByteArrayWrapper(byte[] value) {
        this.value = value;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeBytes(value);
    }

    @Override
    public byte[] read(ByteBuf byteBuf, int offset, int length) {
        value = JtProtocolUtils.readBytes(byteBuf, length);
        return value;
    }

    @Override
    public byte[] value() {
        return new byte[0];
    }
}
