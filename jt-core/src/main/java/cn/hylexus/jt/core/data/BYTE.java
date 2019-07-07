package cn.hylexus.jt.core.data;

/**
 * @author hylexus
 * Created At 2019-07-07 17:15
 */
public class BYTE extends NumericProtocolDataType {
    private byte value;

    private BYTE(final byte value) {
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

    public static BYTE fromBytes(byte[] bytes, int start) {
        assert start >= 0 && start < bytes.length;
        return new BYTE(bytes[start]);
    }

    public static BYTE fromInt(int number) {
        return new BYTE((byte) number);
    }

    public static BYTE fromByte(byte number) {
        return new BYTE(number);
    }

}
