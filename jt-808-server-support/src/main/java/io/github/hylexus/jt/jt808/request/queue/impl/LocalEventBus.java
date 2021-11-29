package io.github.hylexus.jt.jt808.request.queue.impl;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.queue.RequestMsgQueue;

import java.util.concurrent.Executor;

/**
 * @author hylexus
 */
@BuiltinComponent
public class LocalEventBus extends AsyncEventBus implements RequestMsgQueue {

    public LocalEventBus(String identifier, Executor executor) {
        super(identifier, executor);
    }

    public LocalEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        super(executor, subscriberExceptionHandler);
    }

    public LocalEventBus(Executor executor) {
        super(executor);
    }

    @Override
    public void postMsg(Jt808Request metadata) throws Throwable {
        super.post(metadata);
    }
}
