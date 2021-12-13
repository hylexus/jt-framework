package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;

/**
 * @author hylexus
 */
public interface Jt808HandlerAdapter extends OrderedComponent {

    boolean supports(Object handler);

    Jt808HandlerResult handle(Jt808ServerExchange exchange, Object handler) throws Throwable;
}
