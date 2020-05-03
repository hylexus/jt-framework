package io.github.hylexus.jt.data.resp;

import io.github.hylexus.oaks.utils.Bytes;
import lombok.Value;

/**
 * @author hylexus
 * Created At 2019-10-17 10:59 下午
 */
@Value
public class BytesBytesValueWrapper implements BytesValueWrapper<byte[]> {
    byte[] value;

    private BytesBytesValueWrapper(byte[] value, int start, int length) {
        this.value = Bytes.subSequence(value, start, length);
    }

    public static BytesBytesValueWrapper of(byte[] bytes, int start, int length) {
        return new BytesBytesValueWrapper(bytes, start, length);
    }

    public static BytesBytesValueWrapper of(byte[] bytes) {
        return of(bytes, 0, bytes.length);
    }

    @Override
    public byte[] getAsBytes() {
        return value;
    }

}
