package io.github.hylexus.jt.jt808.request.queue.impl;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.queue.RequestMsgQueue;
import io.github.hylexus.jt.jt808.request.queue.RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j
public abstract class AbstractRequestMsgQueueListener<T extends RequestMsgQueue> implements RequestMsgQueueListener {
    protected T queue;
    private final Jt808DispatcherHandler dispatcherHandler;

    public AbstractRequestMsgQueueListener(T queue, Jt808DispatcherHandler dispatcherHandler) {
        this.queue = queue;
        this.dispatcherHandler = dispatcherHandler;
    }

    @Override
    public void consumeMsg(Jt808Request request) {
        this.dispatcherHandler.handleRequest(request);
    }
}
