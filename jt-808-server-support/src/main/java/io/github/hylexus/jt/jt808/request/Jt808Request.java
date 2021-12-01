package io.github.hylexus.jt.jt808.request;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeaderSpec;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808Request {

    MsgType msgType();

    Jt808MsgHeaderSpec header();

    default Jt808ProtocolVersion version() {
        return header().version();
    }

    default int flowId() {
        return header().flowId();
    }

    ByteBuf rawByteBuf();

    ByteBuf body();

    byte originalCheckSum();

    byte calculatedCheckSum();

    @Override
    String toString();
}
