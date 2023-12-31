package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberCreator;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;

public interface Jt1078ChannelCollectorFactory {

    Jt1078ChannelCollector<? extends Jt1078Subscription> create(Class<? extends Jt1078ChannelCollector<?>> cls, Jt1078SubscriberCreator creator);

}
