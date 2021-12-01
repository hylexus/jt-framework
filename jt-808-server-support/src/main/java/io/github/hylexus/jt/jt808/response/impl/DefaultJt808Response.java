package io.github.hylexus.jt.jt808.response.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt808Response implements Jt808Response {

    private final Jt808ProtocolVersion version;
    private final int msgId;
    private final int encryptionType;
    private final int maxPackageSize;
    private final byte reversedBit15InHeader;
    private final String terminalId;
    private final int flowId;
    private final ByteBuf body;

    public DefaultJt808Response(
            Jt808ProtocolVersion version, int msgType,
            int encryptionType, int maxPackageSize,
            byte reversedBit15InHeader,
            String terminalId,
            int flowId, ByteBuf body) {

        this.version = version;
        this.msgId = msgType;
        this.encryptionType = encryptionType;
        this.maxPackageSize = maxPackageSize;
        this.reversedBit15InHeader = reversedBit15InHeader;
        this.terminalId = terminalId;
        this.flowId = flowId;
        this.body = body;
    }

    @Override
    public int msgType() {
        return msgId;
    }

    @Override
    public Jt808ProtocolVersion version() {
        return version;
    }

    @Override
    public String terminalId() {
        return terminalId;
    }

    @Override
    public int flowId() {
        return flowId;
    }

    @Override
    public ByteBuf body() {
        return body;
    }

    @Override
    public int msgBodyLength() {
        return body.readableBytes();
    }

    @Override
    public int encryptionType() {
        return encryptionType;
    }

    @Override
    public int maxPackageSize() {
        return maxPackageSize;
    }

    @Override
    public byte reversedBit15InHeader() {
        return reversedBit15InHeader;
    }
}
