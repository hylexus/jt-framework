package io.github.hylexus.jt808.queue.impl;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt808.queue.RequestMsgQueue;

import java.util.concurrent.Executor;

/**
 * @author hylexus
 * Created At 2019-08-24 14:17
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
    public void dispatchRequestMsg(AbstractRequestMsg msg) {
        super.post(msg);
    }
}
