package io.github.hylexus.jt.jt1078.spec.impl.request;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubPackageIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum DefaultJt1078SubPackageIdentifier implements Jt1078SubPackageIdentifier {
    ATOMIC((byte) 0b0000, "原子包,不可被拆分"),
    FIRST_SUB_PACKAGE((byte) 0b0001, "分包处理时的第一个包"),
    LAST_SUB_PACKAGE((byte) 0b0010, "分包处理时的最后一个包"),
    MIDDLE_SUB_PACKAGE((byte) 0b0011, "分包处理时的中间包"),
    ;
    private final byte value;
    private final String desc;

    DefaultJt1078SubPackageIdentifier(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static final Map<Byte, DefaultJt1078SubPackageIdentifier> MAPPING = new HashMap<>();

    static {
        for (DefaultJt1078SubPackageIdentifier value : values()) {
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
    public Optional<Jt1078SubPackageIdentifier> fromByte(byte value) {
        return Optional.ofNullable(MAPPING.get(value));
    }
}