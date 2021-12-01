package io.github.hylexus.jt.jt808.response.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

public class DefaultJt808ResponseBuilder {
    private Jt808ProtocolVersion version;
    private int msgId;
    private int encryptionType = 0b000;
    private int maxPackageSize = 1024;
    private byte reversedBit15InHeader = 0;
    private String terminalId;
    private Integer flowId;
    private ByteBuf body;

    public static DefaultJt808ResponseBuilder newBuilder() {
        return new DefaultJt808ResponseBuilder();
    }

    public DefaultJt808ResponseBuilder version(Jt808ProtocolVersion version) {
        this.version = version;
        return this;
    }

    public DefaultJt808ResponseBuilder body(ByteBuf body) {
        this.body = body;
        return this;
    }

    public DefaultJt808ResponseBuilder terminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public DefaultJt808ResponseBuilder msgId(MsgType msgType) {
        this.msgId = msgType.getMsgId();
        return this;
    }

    public DefaultJt808ResponseBuilder msgId(int msgId) {
        this.msgId = msgId;
        return this;
    }

    public DefaultJt808ResponseBuilder encryptionType(int encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public DefaultJt808ResponseBuilder flowId(Integer flowId) {
        this.flowId = flowId;
        return this;
    }

    public DefaultJt808ResponseBuilder maxPackageSize(int maxPackageSize) {
        this.maxPackageSize = maxPackageSize;
        return this;
    }

    public DefaultJt808ResponseBuilder reversedBit15InHeader(byte reversedBit15InHeader) {
        this.reversedBit15InHeader = reversedBit15InHeader;
        return this;
    }

    public DefaultJt808Response build() {
        return new DefaultJt808Response(
                Objects.requireNonNull(version, "version() can not be null"),
                Objects.requireNonNull(msgId, "msgType() can not be null"),
                encryptionType,
                maxPackageSize,
                reversedBit15InHeader,
                Objects.requireNonNull(terminalId, "terminalId() can not be null"),
                Objects.requireNonNull(flowId, "flowId() can not be null"),
                Objects.requireNonNull(body, "body() can not be null")
        );
    }
}
