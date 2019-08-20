package io.github.hylexus.jt.jt808.handler;

import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt.jt808.session.Session;

import java.io.IOException;

/**
 * @author hylexus
 * createdAt 2019/2/1
 **/
public interface MsgHandler<T extends AbstractRequestMsg> {

    void handleMsg(T msg, Session session) throws IOException, InterruptedException;

}
