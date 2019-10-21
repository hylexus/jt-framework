package io.github.hylexus.jt.data.resp;

import io.github.hylexus.oaks.utils.Bytes;
import lombok.Value;

/**
 * @author hylexus
 * Created At 2019-10-17 10:59 下午
 */
@Value
public class BytesDataType implements DataType<byte[]> {
    private byte[] value;

    private BytesDataType(byte[] value, int start, int length) {
        this.value = Bytes.subSequence(value, start, length);
    }

    public static BytesDataType of(byte[] bytes, int start, int length) {
        return new BytesDataType(bytes, start, length);
    }

    public static BytesDataType of(byte[] bytes) {
        return of(bytes, 0, bytes.length);
    }

    @Override
    public byte[] getAsBytes() {
        return value;
    }

}
