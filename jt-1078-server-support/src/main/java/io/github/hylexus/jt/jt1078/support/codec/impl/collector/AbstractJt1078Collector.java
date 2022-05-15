package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Sink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
public abstract class AbstractJt1078Collector implements Jt1078Collector {
    protected final List<Jt1078Sink<Jt1078Subscription>> sinks;

    public AbstractJt1078Collector() {
        this.sinks = new ArrayList<>();
    }

    @Override
    public synchronized void addSink(final Jt1078Sink<Jt1078Subscription> sink) {
        this.sinks.add(sink);
    }

    @Override
    public Collection<Jt1078Sink<Jt1078Subscription>> getSinks() {
        return Collections.unmodifiableList(this.sinks);
    }

    @Override
    public synchronized void removeSink(final Jt1078Sink<Jt1078Subscription> sink) {
        this.sinks.remove(sink);
    }

    protected void forEachSink(Consumer<Jt1078Sink<Jt1078Subscription>> consumer) {
        for (Jt1078Sink<Jt1078Subscription> sink : this.sinks) {
            try {
                consumer.accept(sink);
            } catch (Throwable e) {
                sink.error(e);
            }
        }
    }
}
