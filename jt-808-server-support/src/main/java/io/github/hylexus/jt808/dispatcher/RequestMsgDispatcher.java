package io.github.hylexus.jt808.dispatcher;


import io.github.hylexus.jt808.msg.AbstractRequestMsg;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
public interface RequestMsgDispatcher {

    void doDispatch(AbstractRequestMsg abstractMsg) throws Exception;

}
