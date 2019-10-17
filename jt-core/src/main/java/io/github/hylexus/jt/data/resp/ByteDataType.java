package io.github.hylexus.jt.data.resp;

import lombok.Value;

/**
 * @author hylexus
 * Created At 2019-10-17 10:59 下午
 */
@Value
public class ByteDataType implements DataType<byte[]> {
    private byte[] value;

    private ByteDataType(byte[] value) {
        this.value = value;
    }

    public static ByteDataType of(byte[] bytes) {
        return new ByteDataType(bytes);
    }

    @Override
    public byte[] getAsBytes() {
        return value;
    }

}
