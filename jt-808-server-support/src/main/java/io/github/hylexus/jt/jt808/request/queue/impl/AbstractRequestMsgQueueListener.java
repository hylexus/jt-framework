package io.github.hylexus.jt.jt808.request.queue.impl;

import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.request.SubPackageSupportedJt808Request;
import io.github.hylexus.jt.jt808.request.impl.DefaultJt808ServerExchange;
import io.github.hylexus.jt.jt808.request.queue.RequestMsgQueue;
import io.github.hylexus.jt.jt808.request.queue.RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.response.impl.DefaultJt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.github.hylexus.jt.jt808.support.codec.Jt808SubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * @author hylexus
 */
@Slf4j
public abstract class AbstractRequestMsgQueueListener<T extends RequestMsgQueue> implements RequestMsgQueueListener {
    protected T queue;
    private final Jt808DispatcherHandler dispatcherHandler;
    private final Jt808SessionManager sessionManager;
    private final Jt808SubPackageStorage subPackageStorage;

    public AbstractRequestMsgQueueListener(
            T queue, Jt808DispatcherHandler dispatcherHandler,
            Jt808SessionManager sessionManager, Jt808SubPackageStorage subPackageStorage) {
        this.queue = queue;
        this.dispatcherHandler = dispatcherHandler;
        this.sessionManager = sessionManager;
        this.subPackageStorage = subPackageStorage;
    }

    @Override
    public void consumeMsg(Jt808Request request) {
        final Jt808Request requestToDispatch = getRequest(request);
        if (requestToDispatch == null) {
            return;
        }
        if (requestToDispatch != request) {
            JtProtocolUtils.release(request.body(), request.rawByteBuf());
        }

        final String terminalId = requestToDispatch.header().terminalId();
        final Jt808Session session = this.sessionManager.findByTerminalId(terminalId)
                // TODO exception
                .orElseThrow();

        final Jt808Response response = DefaultJt808Response.init(requestToDispatch.version(), terminalId);
        final Jt808ServerExchange exchange = new DefaultJt808ServerExchange(requestToDispatch, response, session);
        try {
            this.dispatcherHandler.handleRequest(exchange);
        } finally {
            if (exchange.response() != response) {
                JtProtocolUtils.release(response.body());
            }
        }
    }

    @Nullable
    private Jt808Request getRequest(Jt808Request request) {
        if (!request.header().msgBodyProps().hasSubPackage()) {
            return request;
        }

        final SubPackageSupportedJt808Request subPackageSupportedJt808Request = (SubPackageSupportedJt808Request) request;
        if (this.subPackageStorage.isAllSubPackagesArrived(subPackageSupportedJt808Request)) {
            final ByteBuf mergedBody = this.subPackageStorage.getAllSubPackages(subPackageSupportedJt808Request);

            final Jt808MsgHeader.Jt808MsgBodyProps newMsgBodyProps = request.header().msgBodyProps()
                    .mutate()
                    .msgBodyLength(mergedBody.readableBytes())
                    .subPackageIdentifier(false)
                    .build();

            final Jt808MsgHeader newHeader = request.header().mutate().msgBodyProps(newMsgBodyProps).build();

            return request.mutate()
                    .header(newHeader)
                    .body(mergedBody)
                    .rawByteBuf(null)
                    .calculatedCheckSum((byte) 0)
                    .originalCheckSum((byte) 0)
                    .build();
        } else {
            this.subPackageStorage.storeSubPackage(subPackageSupportedJt808Request);
            return null;
        }
    }
}
