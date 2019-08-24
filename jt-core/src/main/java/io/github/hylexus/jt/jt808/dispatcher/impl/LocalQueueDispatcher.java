package io.github.hylexus.jt.jt808.dispatcher.impl;

import io.github.hylexus.jt.jt808.dispatcher.AbstractRequestMsgDispatcher;
import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt.jt808.queue.impl.LocalMsgQueue;
import io.github.hylexus.jt.jt808.support.MsgConverterMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
@Slf4j
public class LocalQueueDispatcher extends AbstractRequestMsgDispatcher {

    private RequestMsgQueue msgQueue;

    public LocalQueueDispatcher(MsgConverterMapping msgConverterMapping) {
        super(msgConverterMapping);
        msgQueue = LocalMsgQueue.getInstance();
    }

    @Override
    public void doBroadcast(AbstractRequestMsg msg) throws Exception {
        log.debug("[LocalQueue] receive msg : {}", msg);
        msgQueue.dispatchRequestMsg(msg);
    }
}
