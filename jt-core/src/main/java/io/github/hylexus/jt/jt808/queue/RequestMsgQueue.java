package io.github.hylexus.jt.jt808.queue;

import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;

/**
 * @author hylexus
 * Created At 2019-08-24 16:38
 */
public interface RequestMsgQueue {

    void dispatchRequestMsg(AbstractRequestMsg msg) throws InterruptedException;

}
