package io.github.hylexus.jt.jt1078.spec.impl.subpub;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionType;

/**
 * @author hylexus
 */
public class DefaultJt1078Subscription implements Jt1078Subscription {

    private final Jt1078SubscriptionType type;
    private final byte[] payload;

    public DefaultJt1078Subscription(Jt1078SubscriptionType type, byte[] payload) {
        this.type = type;
        this.payload = payload;
    }

    @Override
    public Jt1078SubscriptionType type() {
        return type;
    }

    @Override
    public byte[] payload() {
        return payload;
    }
}
