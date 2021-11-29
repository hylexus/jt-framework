package io.github.hylexus.jt.jt808.response;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBodySpec;

/**
 * @author hylexus
 */
public interface Jt808Response {

    int msgId();

    Jt808ProtocolVersion version();

    String terminalId();

    int flowId();

    Jt808MsgBodySpec body();

    default int msgBodyLength() {
        return body().data().readableBytes();
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

}
