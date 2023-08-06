package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import lombok.Getter;

@Getter
public enum DefaultJt1078SubscriptionType implements Jt1078Subscription.Jt1078SubscriptionType {
    FLV(1),
    PASS_THROUGH_RAW_DATA(2),
    RAW_DATA(3),
    ;
    private final int value;

    DefaultJt1078SubscriptionType(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return 0;
    }
}