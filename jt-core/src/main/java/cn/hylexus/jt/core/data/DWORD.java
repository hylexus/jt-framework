package cn.hylexus.jt.core.data;

import io.github.hylexus.utils.Bytes;
import io.github.hylexus.utils.IntBitOps;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-07 16:53
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Setter
@Getter
@Accessors(chain = true)
public class DWORD extends NumericProtocolDataType {

    private static final int BYTE_SIZE = 4;
    private final byte[] originalBytes;

    private DWORD(@NonNull final byte[] bytes) {
        this.originalBytes = bytes;
    }

    @Override
    public int getAsInt() {
        return IntBitOps.intFrom4Bytes(originalBytes);
    }

    @Override
    public byte[] toBytes() {
        return Bytes.subSequence(getOriginalBytes(), 0, BYTE_SIZE);
    }

    public static DWORD fromBytes(@NonNull byte[] bytes) {
        return fromBytes(bytes, 0);
    }

    public static DWORD fromBytes(@NonNull byte[] bytes, int start) {
        assert bytes != null && bytes.length > BYTE_SIZE + start;

        return new DWORD(Bytes.subSequence(bytes, start, BYTE_SIZE));
    }

    public static DWORD fromInt(int number) {
        return new DWORD(IntBitOps.intTo4Bytes(number));
    }
}
