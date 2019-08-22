package io.github.hylexus.jt.jt808.queue;

import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
public class LocalMsgQueue {
    private static volatile LocalMsgQueue instance;

    public static LocalMsgQueue getInstance() {
        if (instance == null) {
            synchronized (LocalMsgQueue.class) {
                if (instance == null)
                    instance = new LocalMsgQueue();
            }
        }

        return instance;
    }

    private LinkedBlockingDeque<AbstractRequestMsg> queue = new LinkedBlockingDeque<>();

    public boolean postMsg(AbstractRequestMsg msg, int time, TimeUnit unit) throws InterruptedException {
        return this.queue.offer(msg, time, unit);
    }

    public AbstractRequestMsg getMsg() throws InterruptedException {
        return this.queue.take();
    }
}
