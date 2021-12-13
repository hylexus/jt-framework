package io.github.hylexus.jt.jt808.support.dispatcher.handler;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.MultipleVersionSupport;

/**
 * @author hylexus
 */
public interface Jt808RequestMsgHandler<T> extends MultipleVersionSupport {

    T handleMsg(Jt808ServerExchange exchange);

}
