package io.github.hylexus.jt.jt808.support.dispatcher.handler.result;

import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public abstract class AbstractJt808HandlerResultHandler implements Jt808HandlerResultHandler {

    protected Jt808RequestLifecycleListener requestLifecycleListener;

    @Override
    public void setRequestLifecycleListener(Jt808RequestLifecycleListener requestLifecycleListener) {
        this.requestLifecycleListener = requestLifecycleListener;
    }

    protected boolean shouldSkipResponse(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult, ByteBuf response) {
        final boolean continueProcess = this.requestLifecycleListener.beforeResponse(exchange, handlerResult, response);
        if (!continueProcess) {
            JtProtocolUtils.release(response);
        }
        return !continueProcess;
    }
}
