package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public class DefaultJt808ProtocolVersionDetector implements Jt808ProtocolVersionDetector {

    @Override
    public int getOrder() {
        return OrderedComponent.LOWEST_PRECEDENCE;
    }

    @Override
    public Set<Integer> getSupportedMsgTypes() {
        return Jdk8Adapter.setOf(DEFAULT_VERSION_DETECTOR_MSG_ID);
    }

    @Override
    public Jt808ProtocolVersion detectVersion(int msgId, Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf) {
        if (msgBodyProps.versionIdentifier() != 1) {
            return Jt808ProtocolVersion.VERSION_2013;
        }

        final byte version = byteBuf.getByte(4);
        if (version == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return Jt808ProtocolVersion.VERSION_2019;
        }
        return Jt808ProtocolVersion.VERSION_2013;
    }
}
