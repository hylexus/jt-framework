package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.H264ToFlvJt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.PassThroughJt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.RawDataJt1078ChannelCollector;

public class DefaultCollectorFactory {
    public <S extends Jt1078Subscription> Jt1078ChannelCollector<? extends Jt1078Subscription> create(
            Class<? extends Jt1078ChannelCollector<S>> cls) {

        if (H264ToFlvJt1078ChannelCollector.class.isAssignableFrom(cls)) {
            return new H264ToFlvJt1078ChannelCollector();
        }
        if (RawDataJt1078ChannelCollector.class.isAssignableFrom(cls)) {
            return new RawDataJt1078ChannelCollector();
        }
        if (PassThroughJt1078ChannelCollector.class.isAssignableFrom(cls)) {
            return new PassThroughJt1078ChannelCollector();
        }

        // TODO 动态配置
        throw new UnsupportedOperationException();
    }

    public <S extends Jt1078Subscription> Jt1078ChannelCollector<? extends Jt1078Subscription> defaultCollector() {
        return new H264ToFlvJt1078ChannelCollector();
    }
}
