package io.github.hylexus.jt808.handler;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.support.OrderedComponent;

import java.util.Collections;
import java.util.Set;

/**
 * @author hylexus
 * createdAt 2019/2/1
 **/
public interface MsgHandler<T extends RequestMsgBody> extends OrderedComponent {

    default Set<MsgType> getSupportedMsgTypes() {
        return Collections.emptySet();
    }

    void handleMsg(RequestMsgMetadata metadata, T body, Jt808Session session) throws Throwable;

}
