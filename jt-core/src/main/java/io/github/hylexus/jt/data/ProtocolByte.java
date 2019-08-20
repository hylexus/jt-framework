package io.github.hylexus.jt.data;

/**
 * @author hylexus
 * Created At 2019-07-07 17:15
 */
public class ProtocolByte extends NumericProtocolDataType {
    private byte value;

    private ProtocolByte(final byte value) {
        this.value = value;
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public byte[] getOriginalBytes() {
        return new byte[]{value};
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{value};
    }

    public static ProtocolByte fromBytes(byte[] bytes, int start) {
        assert start >= 0 && start < bytes.length;
        return new ProtocolByte(bytes[start]);
    }

    public static ProtocolByte fromInt(int number) {
        return new ProtocolByte((byte) number);
    }

    public static ProtocolByte fromByte(byte number) {
        return new ProtocolByte(number);
    }

}
