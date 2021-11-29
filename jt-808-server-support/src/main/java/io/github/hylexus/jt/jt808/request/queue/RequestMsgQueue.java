package io.github.hylexus.jt.jt808.request.queue;


import io.github.hylexus.jt.jt808.request.Jt808Request;

/**
 * @author hylexus
 */
public interface RequestMsgQueue {

    void postMsg(Jt808Request metadata) throws Throwable;

    default Jt808Request takeMsg() throws Throwable {
        return null;
    }
}

