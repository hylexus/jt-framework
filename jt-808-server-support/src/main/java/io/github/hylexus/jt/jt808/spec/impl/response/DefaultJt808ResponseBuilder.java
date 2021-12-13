package io.github.hylexus.jt.jt808.spec.impl.response;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.Objects;
import java.util.function.Consumer;

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

    public DefaultJt808ResponseBuilder body(Consumer<Jt808ByteWriter> writerConsumer) {
        if (this.body == null) {
            this.body = ByteBufAllocator.DEFAULT.buffer();
        }
        writerConsumer.accept(Jt808ByteWriter.of(this.body));
        return this;
    }

    public DefaultJt808ResponseBuilder terminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public DefaultJt808ResponseBuilder msgType(MsgType msgType) {
        this.msgId = msgType.getMsgId();
        return this;
    }

    public DefaultJt808ResponseBuilder msgType(int msgId) {
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

    public Jt808Response build() {
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
