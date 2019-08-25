package io.github.hylexus.jt808.handler;

import com.google.common.collect.Sets;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt808.msg.MsgType;
import io.github.hylexus.jt808.session.Session;

import java.io.IOException;
import java.util.Set;

/**
 * @author hylexus
 * createdAt 2019/2/1
 **/
public interface MsgHandler<T extends AbstractRequestMsg> {

    default Set<MsgType> getSupportedMsgTypes() {
        return Sets.newHashSet();
    }

    void handleMsg(T msg, Session session) throws IOException, InterruptedException;

}
