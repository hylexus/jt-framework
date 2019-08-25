package io.github.hylexus.jt808.queue.impl;

import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt808.queue.RequestMsgQueue;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
public class LocalMsgQueue implements RequestMsgQueue {
    private static volatile LocalMsgQueue instance;

    public static LocalMsgQueue getInstance() {
        if (instance == null) {
            synchronized (LocalMsgQueue.class) {
                if (instance == null) {
                    instance = new LocalMsgQueue();
                }
            }
        }

        return instance;
    }

    private LinkedBlockingDeque<AbstractRequestMsg> queue = new LinkedBlockingDeque<>();

    public AbstractRequestMsg getMsg() throws InterruptedException {
        return this.queue.take();
    }

    @Override
    public void dispatchRequestMsg(AbstractRequestMsg msg) throws InterruptedException {
        this.queue.offer(msg, 3, TimeUnit.SECONDS);
    }
}
