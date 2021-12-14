package io.github.hylexus.jt.jt808.support.dispatcher.handler;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.support.dispatcher.MultipleVersionSupport;
import io.github.hylexus.jt.jt808.support.dispatcher.mapping.SimpleJt808RequestHandlerHandlerMapping;

import java.util.Set;

/**
 * @author hylexus
 * @see SimpleJt808RequestHandlerHandlerMapping
 */
public interface SimpleJt808RequestHandler<T> extends MultipleVersionSupport {

    Set<MsgType> getSupportedMsgTypes();

    T handleMsg(Jt808ServerExchange exchange);

}
