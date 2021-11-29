package io.github.hylexus.jt.jt808.request;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBodySpec;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeaderSpec;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808Request {
    Jt808MsgHeaderSpec header();

    Jt808MsgBodySpec bodySpec();

    default int msgBodyStartIndex() {
        return header().msgBodyStartIndex();
    }

    default int msgBodyLength() {
        return header().msgBodyProps().msgBodyLength();
    }

    default Jt808ByteBuf body() {
        return Jt808ByteBuf.from(bodySpec().data().slice(msgBodyStartIndex(), msgBodyLength()));
    }

    byte originalCheckSum();

    byte calculatedCheckSum();

    MsgType msgType();

    default Jt808ProtocolVersion version() {
        return header().version();
    }

    default int flowId() {
        return header().flowId();
    }

    @Override
    String toString();
}
