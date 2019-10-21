package io.github.hylexus.jt.data.resp;

import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.Value;

@Value
public class WordDataType implements DataType<Short> {

    private final Short value;

    private WordDataType(short value) {
        this.value = value;
    }

    public static WordDataType of(int value) {
        return of((byte) value);
    }

    public static WordDataType of(short value) {
        return new WordDataType(value);
    }

    @Override
    public byte[] getAsBytes() {
        return IntBitOps.intTo2Bytes(value);
    }
}