package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import io.github.hylexus.jt.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public interface Jt1078Subscription {

    Jt1078SubscriptionType type();

    Object payload();

    interface Jt1078SubscriptionType {
        int value();
    }

    static ByteArrayJt1078Subscription forByteArray(Jt1078SubscriptionType type, ByteBuf payload) {
        return forByteArray(type, ByteBufUtils.getBytes(payload));
    }

    static ByteArrayJt1078Subscription forByteArray(Jt1078SubscriptionType type, byte[] payload) {
        return new ByteArrayJt1078Subscription(type, payload);
    }

}
