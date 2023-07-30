package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

public abstract class AbstractAsyncChannelCollector<PT extends Jt1078Subscription, IST extends InternalSubscriber<PT>>
        extends AbstractChannelCollector<PT, IST> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final BlockingQueue<Jt1078Request> queue;

    private volatile boolean running = true;
    private final ThreadFactory threadFactory;

    public AbstractAsyncChannelCollector(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        this.queue = new LinkedBlockingQueue<>(1024);
        this.startConsumer();
    }

    @Override
    public void collect(Jt1078Request request) {
        this.queue.add(request.copy());
    }

    @Override
    public void close() {
        this.running = false;
    }

    protected void startConsumer() {
        this.threadFactory.newThread(() -> {
            while (running) {
                Jt1078Request request = null;
                try {
                    request = this.queue.take();
                    this.doCollect(request);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                } finally {
                    if (request != null) {
                        request.release();
                    }
                }
            }
        }).start();
    }

}
