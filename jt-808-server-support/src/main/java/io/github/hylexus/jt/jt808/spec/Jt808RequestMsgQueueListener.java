package io.github.hylexus.jt.jt808.spec;

/**
 * @author hylexus
 */
public interface Jt808RequestMsgQueueListener {

    void consumeMsg(Jt808Request metadata) throws Exception;

}
