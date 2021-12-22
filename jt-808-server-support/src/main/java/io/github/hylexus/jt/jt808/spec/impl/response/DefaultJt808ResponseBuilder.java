package io.github.hylexus.jt.jt808.spec.impl.response;

import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.function.Consumer;

import static io.github.hylexus.jt.utils.Assertions.check;
import static io.github.hylexus.jt.utils.Assertions.requireNonNull;

public class DefaultJt808ResponseBuilder implements Jt808Response.Jt808ResponseBuilder {
    private Jt808ProtocolVersion version;
    private int msgId;
    private int encryptionType = 0b000;
    private int maxPackageSize = 1024;
    private byte reversedBit15InHeader = 0;
    private String terminalId;
    private Integer flowId;
    private ByteBuf body;

    public static Jt808Response.Jt808ResponseBuilder newBuilder() {
        return new DefaultJt808ResponseBuilder();
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder version(Jt808ProtocolVersion version) {
        this.version = version;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder body(ByteBuf body) {
        this.body = body;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder body(Consumer<Jt808ByteWriter> writer) {
        if (this.body == null) {
            this.body = ByteBufAllocator.DEFAULT.buffer();
        }
        writer.accept(Jt808ByteWriter.of(this.body));
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder terminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder msgId(MsgType msgType) {
        this.msgId = msgType.getMsgId();
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder msgId(int msgId) {
        this.msgId = msgId;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder encryptionType(int encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder flowId(Integer flowId) {
        this.flowId = flowId;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder maxPackageSize(int maxPackageSize) {
        this.maxPackageSize = maxPackageSize;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder reversedBit15InHeader(byte reversedBit15InHeader) {
        this.reversedBit15InHeader = reversedBit15InHeader;
        return this;
    }

    @Override
    public Jt808Response build() {
        if (msgId == 0) {
            throw new JtIllegalArgumentException("msgId() can not be null");
        }

        return new DefaultJt808Response(
                requireNonNull(version, "version() can not be null"),
                check(msgId, id -> id > 0, "msgId() can not be null"),
                encryptionType,
                maxPackageSize,
                reversedBit15InHeader,
                requireNonNull(terminalId, "terminalId() can not be null"),
                requireNonNull(flowId, "flowId() can not be null"),
                requireNonNull(body, "body() can not be null")
        );
    }
}
