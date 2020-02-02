package io.github.hylexus.jt808.handler;

import com.google.common.collect.Sets;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.support.OrderedComponent;

import java.io.IOException;
import java.util.Set;

/**
 * @author hylexus
 * createdAt 2019/2/1
 **/
public interface MsgHandler<T extends RequestMsgBody> extends OrderedComponent {

    default Set<MsgType> getSupportedMsgTypes() {
        return Sets.newHashSet();
    }

    void handleMsg(RequestMsgMetadata metadata, T body, Session session) throws IOException, InterruptedException;

}
