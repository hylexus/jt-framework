package io.github.hylexus.jt.jt808.spec;

/**
 * @author hylexus
 */
public interface RequestMsgQueueListener {

    void consumeMsg(Jt808Request metadata) throws Exception;

}
