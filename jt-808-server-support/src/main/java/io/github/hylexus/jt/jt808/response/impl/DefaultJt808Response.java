package io.github.hylexus.jt.jt808.response.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBodySpec;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt808Response extends AbstractJt808Response {
    public DefaultJt808Response(MsgType msgType, ByteBuf body, Jt808Session session) {
        super(msgType, body, session);
    }

    public DefaultJt808Response(MsgType msgType, Jt808MsgBodySpec body, Jt808Session session) {
        super(msgType, body, session);
    }

    public DefaultJt808Response(MsgType msgType, Jt808ProtocolVersion version, String terminalId, int flowId, ByteBuf body) {
        super(msgType, version, terminalId, flowId, body);
    }

    public DefaultJt808Response(MsgType msgType, Jt808ProtocolVersion version, String terminalId, int flowId, Jt808MsgBodySpec body) {
        super(msgType, version, terminalId, flowId, body);
    }
}
