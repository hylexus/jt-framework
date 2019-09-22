package io.github.hylexus.jt.data.converter;

import io.github.hylexus.oaks.utils.IntBitOps;

public class LngLatDataTypeConverter implements DataTypeConverter<Double> {

    @Override
    public Double convert(byte[] bytes) {
        return IntBitOps.intFromBytes(bytes, 0, bytes.length) * 1.0 / 100_0000;
    }

}