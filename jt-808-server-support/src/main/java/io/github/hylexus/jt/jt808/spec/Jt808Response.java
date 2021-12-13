package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808ResponseBuilder;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author hylexus
 */
public interface Jt808Response {

    int DEFAULT_MAX_PACKAGE_SIZE = 1024;

    default ByteBufAllocator allocator() {
        return ByteBufAllocator.DEFAULT;
    }

    int msgType();

    Jt808Response msgType(int msgType);

    Jt808ProtocolVersion version();

    String terminalId();

    int flowId();

    Jt808Response flowId(int flowId);

    default int msgBodyLength() {
        return body().readableBytes();
    }

    default int encryptionType() {
        return 0;
    }

    Jt808Response encryptionType(int encType);

    default int maxPackageSize() {
        return DEFAULT_MAX_PACKAGE_SIZE;
    }

    Jt808Response maxPackageSize(int size);

    default byte reversedBit15InHeader() {
        return 0;
    }

    Jt808Response reversedBit15InHeader(byte reversedBit15InHeader);

    static DefaultJt808ResponseBuilder newBuilder() {
        return DefaultJt808ResponseBuilder.newBuilder();
    }

    ByteBuf body();

    default Jt808Response writeWord(int value) {
        JtProtocolUtils.writeWord(body(), value);
        return this;
    }

    default Jt808Response writeDWord(int value) {
        JtProtocolUtils.writeDword(body(), value);
        return this;
    }

    default Jt808Response writeByte(int value) {
        body().writeByte(value);
        return this;
    }

    default Jt808Response writeBytes(ByteBuf byteBuf) {
        body().writeBytes(byteBuf);
        return this;
    }

    default Jt808Response writeBytes(byte[] bytes) {
        body().writeBytes(bytes);
        return this;
    }

    default Jt808Response writeBcd(String bcd) {
        JtProtocolUtils.writeBcd(body(), bcd);
        return this;
    }

    default Jt808Response clear() {
        body().clear();
        return this;
    }
}
