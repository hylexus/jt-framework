package io.github.hylexus.jt.jt1078.spec.impl.request;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum DefaultJt1078SubscriptionType implements Jt1078SubscriptionType {
    RAW(-1, "Raw bytes from client"),
    FLV(1, ".flv"),
    MP4(2, ".mp4"),
    ;
    private final int id;
    private final String desc;

    DefaultJt1078SubscriptionType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    private static final Map<Integer, DefaultJt1078SubscriptionType> MAPPING = new HashMap<>();

    static {
        for (DefaultJt1078SubscriptionType value : values()) {
            MAPPING.put(value.id, value);
        }
    }

    @Override
    public int code() {
        return id;
    }

    @Override
    public String desc() {
        return desc;
    }

    public static Optional<DefaultJt1078SubscriptionType> findByCode(int code) {
        return Optional.ofNullable(MAPPING.get(code));
    }
}