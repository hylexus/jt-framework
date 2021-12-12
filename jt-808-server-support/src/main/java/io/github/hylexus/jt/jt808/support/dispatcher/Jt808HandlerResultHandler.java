package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.session.Jt808Session;

public interface Jt808HandlerResultHandler extends OrderedComponent {

    boolean supports(Jt808HandlerResult handlerResult);

    void handleResult(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult);

}