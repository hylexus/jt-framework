package io.github.hylexus.jt.jt1078.support.dispatcher;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;

/**
 * @author hylexus
 */
public interface Jt1078RequestMsgDispatcher {

    void doDispatch(Jt1078Request request) throws Throwable;

}
