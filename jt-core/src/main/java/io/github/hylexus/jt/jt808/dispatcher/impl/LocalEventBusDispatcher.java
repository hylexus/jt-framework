package io.github.hylexus.jt.jt808.dispatcher.impl;

import io.github.hylexus.jt.jt808.dispatcher.AbstractRequestMsgDispatcher;
import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt.jt808.support.MsgConverterMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-24 14:10
 */
@Slf4j
public class LocalEventBusDispatcher extends AbstractRequestMsgDispatcher {

    private RequestMsgQueue eventBus;

    public LocalEventBusDispatcher(MsgConverterMapping msgConverterMapping, RequestMsgQueue eventBus) {
        super(msgConverterMapping);
        this.eventBus = eventBus;
    }

    @Override
    public void doBroadcast(AbstractRequestMsg msg) throws Exception {
        log.debug("[EventBus] receive msg : {}", msg);
        eventBus.dispatchRequestMsg(msg);
    }
}
