package io.github.hylexus.jt.data.resp;

import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.Value;

@Value
public class WordBytesValueWrapper implements BytesValueWrapper<Short> {

    private final Short value;

    private WordBytesValueWrapper(short value) {
        this.value = value;
    }

    public static WordBytesValueWrapper of(int value) {
        return of((byte) value);
    }

    public static WordBytesValueWrapper of(short value) {
        return new WordBytesValueWrapper(value);
    }

    @Override
    public byte[] getAsBytes() {
        return IntBitOps.intTo2Bytes(value);
    }
}