package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberCreator;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.H264ToFlvJt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.PassThroughJt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.RawDataJt1078ChannelCollector;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ThreadFactory;

public class DefaultCollectorFactory implements Jt1078ChannelCollectorFactory {

    private static final ThreadFactory H264_TO_FLV_THREAD_FACTORY = new DefaultThreadFactory("H264ToFlv");

    @Override
    public Jt1078ChannelCollector<? extends Jt1078Subscription> create(Class<? extends Jt1078ChannelCollector<?>> cls, Jt1078SubscriberCreator creator) {

        if (H264ToFlvJt1078ChannelCollector.class.isAssignableFrom(cls)) {
            return new H264ToFlvJt1078ChannelCollector(H264_TO_FLV_THREAD_FACTORY, creator);
        }

        if (RawDataJt1078ChannelCollector.class.isAssignableFrom(cls)) {
            return new RawDataJt1078ChannelCollector();
        }

        if (PassThroughJt1078ChannelCollector.class.isAssignableFrom(cls)) {
            return new PassThroughJt1078ChannelCollector();
        }

        throw new UnsupportedOperationException();
    }
}
