package io.github.hylexus.jt.data;

import io.github.hylexus.oaks.utils.Bytes;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-07 17:47
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProtocolString implements ProtocolDataType {
    private byte[] originalBytes;
    private int count;

    public ProtocolString(byte[] originalBytes) {
        this.originalBytes = originalBytes;
        this.count = originalBytes.length;
    }

    public static ProtocolString fromBytes(byte[] bytes, int count) {
        return fromBytes(bytes, 0, count);
    }

    public static ProtocolString fromBytes(byte[] bytes, int start, int count) {
        return new ProtocolString(Bytes.subSequence(bytes, start, count));
    }

    public String getAsString() {
        return new String(this.toBytes());
    }

    @Override
    public byte[] toBytes() {
        return Bytes.subSequence(getOriginalBytes(), 0, count);
    }
}
