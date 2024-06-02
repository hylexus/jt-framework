package io.github.hylexus.jt.jt808.spec.impl.response;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteWriter;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Assertions;
import io.github.hylexus.oaks.utils.Numbers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.function.Consumer;

import static io.github.hylexus.jt.utils.Assertions.assertThat;
import static io.github.hylexus.jt.utils.Assertions.requireNonNull;

public class DefaultJt808ResponseBuilder implements Jt808Response.Jt808ResponseBuilder {
    private Jt808ProtocolVersion version;
    private int msgId;
    private int encryptionType = 0b000;
    private int maxPackageSize = Jt808Response.DEFAULT_MAX_PACKAGE_SIZE;
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
    public Jt808Response.Jt808ResponseBuilder encryptionType(int encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    @Override
    public Jt808Response.Jt808ResponseBuilder body(ByteBuf body, boolean autoRelease) {
        final ByteBuf old = this.body;
        try {
            this.body = body;
            return this;
        } finally {
            if (autoRelease) {
                JtProtocolUtils.release(old);
            }
        }
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
    public Jt808Response.Jt808ResponseBuilder msgId(int msgId) {
        this.msgId = Assertions.assertThat(msgId, Numbers::isPositive, "msg > 0");
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
        return new DefaultJt808Response(
                requireNonNull(version, "version() can not be null"),
                assertThat(msgId, id -> id > 0, "msgId() can not be null"),
                encryptionType,
                maxPackageSize,
                reversedBit15InHeader,
                requireNonNull(terminalId, "terminalId() can not be null"),
                flowId == null ? -1 : flowId,
                requireNonNull(body, "body() can not be null")
        );
    }
}
