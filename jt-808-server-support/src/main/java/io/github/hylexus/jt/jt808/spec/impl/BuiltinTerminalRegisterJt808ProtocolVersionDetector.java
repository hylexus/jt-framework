package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class BuiltinTerminalRegisterJt808ProtocolVersionDetector implements Jt808ProtocolVersionDetector {
    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public Set<Integer> getSupportedMsgTypes() {
        return Jdk8Adapter.setOf(BuiltinJt808MsgType.CLIENT_REGISTER.getMsgId());
    }

    @Override
    public Jt808ProtocolVersion detectVersion(int msgId, Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf) {
        if (isVersion2019(msgBodyProps, byteBuf)) {
            return Jt808ProtocolVersion.VERSION_2019;
        }
        if (msgBodyProps.msgBodyLength() > 37) {
            return Jt808ProtocolVersion.VERSION_2013;
        }
        return Jt808ProtocolVersion.VERSION_2011;
    }
}
