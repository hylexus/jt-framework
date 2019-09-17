package io.github.hylexus.jt808.queue;


import io.github.hylexus.jt808.msg.AbstractRequestMsg;

/**
 * @author hylexus
 * Created At 2019-08-24 16:38
 */
public interface RequestMsgQueue {

    void postMsg(AbstractRequestMsg msg) throws InterruptedException;

    default AbstractRequestMsg takeMsg() throws InterruptedException {
        return null;
    }
}

