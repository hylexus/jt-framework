package io.github.hylexus.jt808.queue;


import io.github.hylexus.jt808.msg.RequestMsgWrapper;

/**
 * @author hylexus
 * Created At 2019-08-24 16:38
 */
public interface RequestMsgQueue {

    void postMsg(RequestMsgWrapper wrapper) throws InterruptedException;

    default RequestMsgWrapper takeMsg() throws InterruptedException {
        return null;
    }
}

