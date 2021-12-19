package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author hylexus
 */
public interface Jt808ProtocolVersionDetector extends ReplaceableComponent {

    int DEFAULT_VERSION_DETECTOR_MSG_ID = Integer.MIN_VALUE;

    Set<Integer> getSupportedMsgTypes();

    Jt808ProtocolVersion detectVersion(int msgId, Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf);

    default boolean isVersion2019(Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf) {
        return msgBodyProps.versionIdentifier() == 1;
    }

    @Override
    default int getOrder() {
        return ReplaceableComponent.DEFAULT_ORDER;
    }

}
