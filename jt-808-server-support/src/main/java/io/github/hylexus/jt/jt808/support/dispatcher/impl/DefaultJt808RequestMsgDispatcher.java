package io.github.hylexus.jt.jt808.support.dispatcher.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-08-24 14:10
 */
@Slf4j
@BuiltinComponent
public class DefaultJt808RequestMsgDispatcher implements Jt808RequestMsgDispatcher, Jt808RequestLifecycleListenerAware {

    private final Jt808RequestMsgQueueListener queueListener;
    private Jt808RequestLifecycleListener requestLifecycleListener;

    public DefaultJt808RequestMsgDispatcher(Jt808RequestMsgQueueListener queueListener) {
        this.queueListener = queueListener;
    }

    @Override
    public void doDispatch(Jt808Request request) throws Throwable {
        try {
            final boolean continueProcess = this.requestLifecycleListener.beforeDispatch(request);
            if (!continueProcess) {
                return;
            }
            this.queueListener.consumeMsg(request);
        } finally {
            if (request != null) {
                request.release();
            }
        }
    }

    @Override
    public void setRequestLifecycleListener(Jt808RequestLifecycleListener requestLifecycleListener) {
        this.requestLifecycleListener = requestLifecycleListener;
    }
}
