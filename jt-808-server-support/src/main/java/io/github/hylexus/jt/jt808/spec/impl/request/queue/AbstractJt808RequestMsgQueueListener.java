package io.github.hylexus.jt.jt808.spec.impl.request.queue;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j
public class AbstractJt808RequestMsgQueueListener implements Jt808RequestMsgQueueListener {
    private final Jt808DispatcherHandler dispatcherHandler;
    private final Jt808SessionManager sessionManager;
    private final Jt808RequestSubPackageStorage subPackageStorage;
    private final Jt808RequestSubPackageEventListener requestSubPackageEventListener;

    public AbstractJt808RequestMsgQueueListener(
            Jt808DispatcherHandler dispatcherHandler,
            Jt808SessionManager sessionManager, Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener) {
        this.dispatcherHandler = dispatcherHandler;
        this.sessionManager = sessionManager;
        this.subPackageStorage = subPackageStorage;
        this.requestSubPackageEventListener = requestSubPackageEventListener;
    }

    @Override
    public void consumeMsg(Jt808Request originalRequest) {
        if (originalRequest.header().msgBodyProps().hasSubPackage()) {
            this.subPackageStorage.saveSubPackage(originalRequest);
            this.requestSubPackageEventListener.onSubPackage(originalRequest);
            return;
        }
        Jt808ServerExchange exchange = null;
        Jt808Response originalResponse = null;
        try {
            final String terminalId = originalRequest.terminalId();
            final Jt808Session session = this.sessionManager.findByTerminalId(terminalId)
                    // TODO exception
                    .orElseThrow();

            originalResponse = DefaultJt808Response.init(originalRequest.version(), originalRequest.terminalId());
            exchange = new DefaultJt808ServerExchange(originalRequest, originalResponse, session);
            this.dispatcherHandler.handleRequest(exchange);
        } finally {
            if (exchange != null && exchange.response() != originalResponse) {
                originalResponse.release();
            }
        }
    }

}
