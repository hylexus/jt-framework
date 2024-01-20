package io.github.hylexus.jt.jt808.spec.impl.request.queue;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808RequestFilter;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808RequestFilterChain;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;

import java.util.List;

/**
 * @author hylexus
 */
@BuiltinComponent
public class FilteringJt808RequestMsgQueueListener extends AbstractJt808RequestMsgQueueListener {

    private final DefaultJt808RequestFilterChain chain;

    public FilteringJt808RequestMsgQueueListener(
            Jt808DispatcherHandler dispatcherHandler,
            // Jt808SessionManager sessionManager,
            Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener, List<Jt808RequestFilter> filterList) {

        super(dispatcherHandler, subPackageStorage, requestSubPackageEventListener);
        this.chain = new DefaultJt808RequestFilterChain(filterList, dispatcherHandler);
    }

    @Override
    protected void handleRequest(Jt808ServerExchange exchange) {
        this.chain.filter(exchange);
    }
}
