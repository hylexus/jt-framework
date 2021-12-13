package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.spec.impl.request.DefaultJt808Request;
import io.netty.buffer.ByteBuf;

import java.util.Map;

/**
 * @author hylexus
 */
public interface Jt808Request {

    MsgType msgType();

    Jt808RequestHeader header();

    default String terminalId() {
        return header().terminalId();
    }

    default Jt808ProtocolVersion version() {
        return header().version();
    }

    default int flowId() {
        return header().flowId();
    }

    ByteBuf rawByteBuf();

    ByteBuf body();

    default int msgBodyLength() {
        return header().msgBodyLength();
    }

    byte originalCheckSum();

    byte calculatedCheckSum();

    Map<String, Object> getAttributes();

    default Jt808RequestBuilder mutate() {
        return new DefaultJt808Request.DefaultJt808RequestBuilder(this);
    }

    @Override
    String toString();

    interface Jt808RequestBuilder {

        Jt808RequestBuilder header(Jt808RequestHeader header);

        Jt808RequestBuilder rawByteBuf(ByteBuf byteBuf);

        Jt808RequestBuilder body(ByteBuf body);

        Jt808RequestBuilder originalCheckSum(byte checkSum);

        Jt808RequestBuilder calculatedCheckSum(byte checkSum);

        Jt808Request build();
    }
}
