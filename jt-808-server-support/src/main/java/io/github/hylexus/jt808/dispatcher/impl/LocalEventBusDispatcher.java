package io.github.hylexus.jt808.dispatcher.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.dispatcher.AbstractRequestMsgDispatcher;
import io.github.hylexus.jt808.msg.RequestMsgWrapper;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-24 14:10
 */
@Slf4j
@BuiltinComponent
public class LocalEventBusDispatcher extends AbstractRequestMsgDispatcher {

    private final RequestMsgQueue eventBus;

    public LocalEventBusDispatcher(RequestMsgBodyConverterMapping msgConverterMapping, RequestMsgQueue eventBus) {
        super(msgConverterMapping);
        this.eventBus = eventBus;
    }

    @Override
    public void doBroadcast(RequestMsgWrapper wrapper) throws Exception {
        log.debug("[EventBus] receive msg : {}", wrapper);
        eventBus.postMsg(wrapper);
    }
}
