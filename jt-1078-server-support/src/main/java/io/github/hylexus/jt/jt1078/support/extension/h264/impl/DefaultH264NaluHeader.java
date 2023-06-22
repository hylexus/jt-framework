package io.github.hylexus.jt.jt1078.support.extension.h264.impl;

import io.github.hylexus.jt.jt1078.support.extension.h264.H264NaluHeader;
import lombok.Getter;

@Getter
public class DefaultH264NaluHeader implements H264NaluHeader {

    private final byte forbiddenBit;

    private final byte nalRefIdc;

    private final byte typeValue;

    public DefaultH264NaluHeader(int value) {
        this.forbiddenBit = (byte) (value >>> 7);
        this.nalRefIdc = (byte) ((value & 0b01100000) >> 5);
        this.typeValue = (byte) (value & 0b11111);
    }

    @Override
    public byte forbiddenBit() {
        return this.forbiddenBit;
    }

    @Override
    public byte nalRefIdc() {
        return this.nalRefIdc;
    }

    @Override
    public byte typeValue() {
        return this.typeValue;
    }

    @Override
    public String toString() {
        return "NaluHeader{"
                + "F=" + forbiddenBit()
                + ", NRI=" + nir()
                + ", TYPE=" + type().orElse(null)
                + '}';
    }
}
