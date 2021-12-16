package io.github.hylexus.jt.jt808.support.dispatcher.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueue;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-24 14:10
 */
@Slf4j
@BuiltinComponent
public class LocalEventBusDispatcher implements Jt808RequestMsgDispatcher {

    private final Jt808RequestMsgQueue eventBus;

    public LocalEventBusDispatcher(Jt808RequestMsgQueue eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void doDispatch(Jt808Request request) throws Throwable {
        this.eventBus.postMsg(request);
    }
}
