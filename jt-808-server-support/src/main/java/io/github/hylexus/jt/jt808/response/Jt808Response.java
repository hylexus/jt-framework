package io.github.hylexus.jt.jt808.response;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.response.impl.DefaultJt808ResponseBuilder;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808Response {

    int msgType();

    Jt808ProtocolVersion version();

    String terminalId();

    int flowId();

    ByteBuf body();

    default int msgBodyLength() {
        return body().readableBytes();
    }

    default int encryptionType() {
        return 0;
    }

    default int maxPackageSize() {
        return 1024;
    }

    default byte reversedBit15InHeader() {
        return 0;
    }

    static DefaultJt808ResponseBuilder newBuilder() {
        return DefaultJt808ResponseBuilder.newBuilder();
    }
}
