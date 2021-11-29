package io.github.hylexus.jt.jt808.request.queue;

import io.github.hylexus.jt.jt808.request.Jt808Request;

/**
 * @author hylexus
 */
public interface RequestMsgQueueListener {

    void consumeMsg(Jt808Request metadata) throws Exception;

}
