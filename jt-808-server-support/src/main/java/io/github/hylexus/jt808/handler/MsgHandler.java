package io.github.hylexus.jt808.handler;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.support.OrderedComponent;

import java.util.Collections;
import java.util.Set;

/**
 * createdAt 2019/2/1
 *
 * @author hylexus
 **/
public interface MsgHandler<T extends RequestMsgBody> extends OrderedComponent {

    default Set<MsgType> getSupportedMsgTypes() {
        return Collections.emptySet();
    }

    /**
     * @return 该处理器可以处理的协议版本号
     * @author hylexus
     * @since 2.0.0
     */
    default Set<Jt808ProtocolVersion> getSupportedProtocolVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersionAutoDetection();
    }

    void handleMsg(RequestMsgMetadata metadata, T body, Jt808Session session) throws Throwable;

}
