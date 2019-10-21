package io.github.hylexus.jt.data.resp;

/**
 * @author hylexus
 * Created At 2019-10-19 7:51 下午
 */
public class ByteDataType implements DataType<Byte> {
    private byte value;

    private ByteDataType(byte value) {
        this.value = value;
    }

    public static ByteDataType of(int value) {
        return of((byte) value);
    }

    public static ByteDataType of(byte value) {
        return new ByteDataType(value);
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
