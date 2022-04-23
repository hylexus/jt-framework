package io.github.hylexus.jt.jt1078.spec.impl.request;

import io.github.hylexus.jt.jt1078.spec.Jt1078DataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 */
public enum DefaultJt1078DataType implements Jt1078DataType {
    VIDEO_I((byte) 0b0000, "视频I帧"),
    VIDEO_P((byte) 0b0001, "视频P帧"),
    VIDEO_B((byte) 0b0010, "视频B帧"),
    AUDIO((byte) 0b0011, "音频帧"),
    TRANSPARENT_TRANSMISSION((byte) 0b0100, "透传数据"),
    ;
    private final byte value;
    private final String desc;

    DefaultJt1078DataType(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static final Map<Byte, DefaultJt1078DataType> MAPPING = new HashMap<>();

    static {
        for (DefaultJt1078DataType value : values()) {
            MAPPING.put(value.value, value);
        }
    }

    @Override
    public byte value() {
        return value;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public Optional<Jt1078DataType> fromByte(byte value) {
        return Optional.ofNullable(MAPPING.get(value));
    }
}
