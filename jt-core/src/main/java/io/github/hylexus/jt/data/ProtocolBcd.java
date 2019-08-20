package io.github.hylexus.jt.data;

import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
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
public class ProtocolBcd implements ProtocolDataType {

    private byte[] originalBytes;
    private int count;

    private ProtocolBcd(byte[] originalBytes) {
        this.originalBytes = originalBytes;
        this.count = originalBytes.length;
    }

    public static ProtocolBcd fromBytes(byte[] bytes, int count) {
        return fromBytes(bytes, 0, count);
    }

    public static ProtocolBcd fromBytes(byte[] bytes, int start, int count) {
        return new ProtocolBcd(Bytes.subSequence(bytes, start, count));
    }

    public String getAsString() {
        return BcdOps.bcd2String(toBytes());
    }

    @Override
    public byte[] toBytes() {
        return Bytes.subSequence(getOriginalBytes(), 0, count);
    }
}
