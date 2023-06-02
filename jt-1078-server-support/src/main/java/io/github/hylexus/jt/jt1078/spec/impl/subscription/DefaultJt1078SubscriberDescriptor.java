package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;

import java.time.LocalDateTime;

public class DefaultJt1078SubscriberDescriptor implements Jt1078SubscriberDescriptor {
    private final String id;
    private final String sim;

    private final short channel;
    private final LocalDateTime createdAt;

    private final String desc;

    public DefaultJt1078SubscriberDescriptor(String id, String sim, short channel, LocalDateTime createdAt, String desc) {
        this.id = id;
        this.sim = sim;
        this.channel = channel;
        this.createdAt = createdAt;
        this.desc = desc;
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getSim() {
        return this.sim;
    }

    @Override
    public short getChannel() {
        return this.channel;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public String getDesc() {
        if (this.desc == null) {
            return Jt1078SubscriberDescriptor.super.getDesc();
        }
        return this.desc;
    }
}
