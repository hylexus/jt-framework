package io.github.hylexus.jt.data.resp;

import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.Value;

@Value
public class DwordBytesValueWrapper implements BytesValueWrapper<Integer> {
    Integer value;

    private DwordBytesValueWrapper(int value) {
        this.value = value;
    }

    public static DwordBytesValueWrapper of(int value) {
        return new DwordBytesValueWrapper(value);
    }

    @Override
    public byte[] getAsBytes() {
        return IntBitOps.intTo4Bytes(value);
    }
}