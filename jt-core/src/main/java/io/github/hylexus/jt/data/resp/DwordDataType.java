package io.github.hylexus.jt.data.resp;

import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.Value;

@Value
public class DwordDataType implements DataType<Integer> {
    private final int value;

    private DwordDataType(int value) {
        this.value = value;
    }

    public static DwordDataType of(int value) {
        return new DwordDataType(value);
    }

    @Override
    public byte[] getAsBytes() {
        return IntBitOps.intTo4Bytes(value);
    }
}