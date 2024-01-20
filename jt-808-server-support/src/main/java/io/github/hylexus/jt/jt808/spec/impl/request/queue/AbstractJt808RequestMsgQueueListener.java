package io.github.hylexus.jt.jt808.spec.impl.request.queue;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author hylexus
 */
@Slf4j
public class AbstractJt808RequestMsgQueueListener implements Jt808RequestMsgQueueListener {
    private final Jt808DispatcherHandler dispatcherHandler;
    private final Jt808RequestSubPackageStorage subPackageStorage;
    private final Jt808RequestSubPackageEventListener requestSubPackageEventListener;

    public AbstractJt808RequestMsgQueueListener(
            Jt808DispatcherHandler dispatcherHandler,
            Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener) {
        this.dispatcherHandler = dispatcherHandler;
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
            final Jt808Session session = Objects.requireNonNull(originalRequest.session(), "session is null");

            originalResponse = DefaultJt808Response.init(originalRequest.version(), originalRequest.terminalId());
            exchange = new DefaultJt808ServerExchange(originalRequest, originalResponse, session);
            this.handleRequest(exchange);
        } finally {
            if (exchange != null && exchange.response() != originalResponse) {
                originalResponse.release();
            }
        }
    }

    protected void handleRequest(Jt808ServerExchange exchange) {
        this.dispatcherHandler.handleRequest(exchange);
    }

}
