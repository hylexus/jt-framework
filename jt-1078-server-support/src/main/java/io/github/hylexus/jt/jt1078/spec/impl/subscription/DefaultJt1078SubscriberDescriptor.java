package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;

public class DefaultJt1078SubscriberDescriptor implements Jt1078SubscriberDescriptor {
    private final String id;
    private final String sim;

    private final short channel;
    private final String desc;

    public DefaultJt1078SubscriberDescriptor(String id, String sim, short channel, String desc) {
        this.id = id;
        this.sim = sim;
        this.channel = channel;
        this.desc = desc;
    }


    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String sim() {
        return this.sim;
    }

    @Override
    public short channel() {
        return this.channel;
    }

    @Override
    public String desc() {
        if (this.desc == null) {
            return Jt1078SubscriberDescriptor.super.desc();
        }
        return this.desc;
    }
}
