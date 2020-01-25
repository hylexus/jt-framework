package io.github.hylexus.jt.data.resp;

/**
 * @author hylexus
 * Created At 2019-10-19 7:51 下午
 */
public class ByteBytesValueWrapper implements BytesValueWrapper<Byte> {
    private byte value;

    private ByteBytesValueWrapper(byte value) {
        this.value = value;
    }

    public static ByteBytesValueWrapper of(int value) {
        return of((byte) value);
    }

    public static ByteBytesValueWrapper of(byte value) {
        return new ByteBytesValueWrapper(value);
    }

    @Override
    public byte[] getAsBytes() {
        return new byte[value];
    }

    @Override
    public Byte getValue() {
        return value;
    }
}
