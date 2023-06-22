package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;

public class ByteArrayJt1078Subscription implements Jt1078Subscription {
    private final Jt1078SubscriptionType type;
    private final byte[] payload;

    public ByteArrayJt1078Subscription(Jt1078SubscriptionType type, byte[] payload) {
        this.type = type;
        this.payload = payload;
    }

    @Override
    public Jt1078SubscriptionType type() {
        return this.type;
    }

    @Override
    public byte[] payload() {
        return this.payload;
    }
}