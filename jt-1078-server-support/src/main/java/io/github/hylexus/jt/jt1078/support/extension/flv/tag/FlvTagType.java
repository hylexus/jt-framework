package io.github.hylexus.jt.jt1078.support.extension.flv.tag;

import lombok.Getter;

@Getter
public enum FlvTagType {
    AUDIO((byte) 0x08),
    VIDEO((byte) 0x09),
    SCRIPT_DATA((byte) 0x12),
    ;
    private final byte value;

    FlvTagType(byte value) {
        this.value = value;
    }

}