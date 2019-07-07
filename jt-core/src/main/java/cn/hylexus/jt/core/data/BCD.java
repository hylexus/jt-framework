package cn.hylexus.jt.core.data;

import io.github.hylexus.utils.BcdOps;
import io.github.hylexus.utils.Bytes;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-07 17:39
 */
@Getter
@Setter
@Accessors(chain = true)
public class BCD implements ProtocolDataType {

    private byte[] originalBytes;
    private int count;

    private BCD(byte[] originalBytes) {
        this.originalBytes = originalBytes;
        this.count = originalBytes.length;
    }

    public static BCD fromBytes(byte[] bytes, int count) {
        return fromBytes(bytes, 0, count);
    }

    public static BCD fromBytes(byte[] bytes, int start, int count) {
        return new BCD(Bytes.subSequence(bytes, start, count));
    }

    public String getAsString() {
        return BcdOps.bcd2String(toBytes());
    }

    @Override
    public byte[] toBytes() {
        return Bytes.subSequence(getOriginalBytes(), 0, count);
    }
}
