package io.github.hylexus.jt.jt808.support.dispatcher;


import io.github.hylexus.jt.jt808.spec.Jt808Request;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
public interface Jt808RequestMsgDispatcher {

    void doDispatch(Jt808Request request) throws Throwable;

}
