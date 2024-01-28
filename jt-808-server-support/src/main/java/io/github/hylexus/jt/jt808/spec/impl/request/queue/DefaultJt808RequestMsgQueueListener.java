package io.github.hylexus.jt.jt808.spec.impl.request.queue;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;

/**
 * @author hylexus
 */
@BuiltinComponent
public class DefaultJt808RequestMsgQueueListener extends AbstractJt808RequestMsgQueueListener {

    public DefaultJt808RequestMsgQueueListener(
            Jt808DispatcherHandler dispatcherHandler,
            // Jt808SessionManager sessionManager,
            Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener) {

        super(dispatcherHandler, subPackageStorage, requestSubPackageEventListener);
    }

}
