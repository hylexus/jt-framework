package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;

public interface Jt808HandlerResultHandler extends OrderedComponent, Jt808RequestLifecycleListenerAware {

    boolean supports(Jt808HandlerResult handlerResult);

    void handleResult(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult);

}