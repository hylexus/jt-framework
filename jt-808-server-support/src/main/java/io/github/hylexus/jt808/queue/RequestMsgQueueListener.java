package io.github.hylexus.jt808.queue;

import io.github.hylexus.jt808.msg.AbstractRequestMsg;

import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-08-25 18:29
 */
public interface RequestMsgQueueListener {

    void consumeMsg(AbstractRequestMsg msg) throws IOException, InterruptedException;

}
