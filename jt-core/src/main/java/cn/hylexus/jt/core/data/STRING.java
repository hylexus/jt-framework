package cn.hylexus.jt.core.data;

import io.github.hylexus.utils.Bytes;
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
public class STRING implements ProtocolDataType {
    private byte[] originalBytes;
    private int count;

    public STRING(byte[] originalBytes) {
        this.originalBytes = originalBytes;
        this.count = originalBytes.length;
    }

    public static STRING fromBytes(byte[] bytes, int count) {
        return fromBytes(bytes, 0, count);
    }

    public static STRING fromBytes(byte[] bytes, int start, int count) {
        return new STRING(Bytes.subSequence(bytes, start, count));
    }

    public String getAsString() {
        return new String(this.toBytes());
    }

    @Override
    public byte[] toBytes() {
        return Bytes.subSequence(getOriginalBytes(), 0, count);
    }
}
