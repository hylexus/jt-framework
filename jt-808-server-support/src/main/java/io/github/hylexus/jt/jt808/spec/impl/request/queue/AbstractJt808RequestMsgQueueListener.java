package io.github.hylexus.jt.jt808.spec.impl.request.queue;

import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * @author hylexus
 */
@Slf4j
public abstract class AbstractJt808RequestMsgQueueListener<T extends Jt808RequestMsgQueue> implements Jt808RequestMsgQueueListener {
    protected T queue;
    private final Jt808DispatcherHandler dispatcherHandler;
    private final Jt808SessionManager sessionManager;
    private final Jt808RequestSubPackageStorage subPackageStorage;

    public AbstractJt808RequestMsgQueueListener(
            T queue, Jt808DispatcherHandler dispatcherHandler,
            Jt808SessionManager sessionManager, Jt808RequestSubPackageStorage subPackageStorage) {
        this.queue = queue;
        this.dispatcherHandler = dispatcherHandler;
        this.sessionManager = sessionManager;
        this.subPackageStorage = subPackageStorage;
    }

    @Override
    public void consumeMsg(Jt808Request originalRequest) {
        Jt808ServerExchange exchange = null;
        Jt808Request requestToDispatch = null;
        Jt808Response originalResponse = null;
        try {
            requestToDispatch = this.getRequest(originalRequest);
            if (requestToDispatch == null) {
                return;
            }
            if (requestToDispatch != originalRequest) {
                JtProtocolUtils.release(originalRequest.body(), originalRequest.rawByteBuf());
            }

            final String terminalId = requestToDispatch.terminalId();
            final Jt808Session session = this.sessionManager.findByTerminalId(terminalId)
                    // TODO exception
                    .orElseThrow();

            originalResponse = DefaultJt808Response.init(originalRequest.version(), originalRequest.terminalId());
            exchange = new DefaultJt808ServerExchange(requestToDispatch, originalResponse, session);
            this.dispatcherHandler.handleRequest(exchange);
        } finally {
            if (requestToDispatch != null) {
                JtProtocolUtils.release(requestToDispatch.body(), requestToDispatch.rawByteBuf());
            }

            if (exchange != null && exchange.response() != originalResponse) {
                JtProtocolUtils.release(originalResponse.body());
            }
        }
    }

    @Nullable
    private Jt808Request getRequest(Jt808Request request) {
        if (!request.header().msgBodyProps().hasSubPackage()) {
            return request;
        }

        final Jt808SubPackageRequest subPackageRequest = (Jt808SubPackageRequest) request;
        this.subPackageStorage.saveSubPackage(subPackageRequest);
        return null;
    }
}
