package io.github.hylexus.jt.jt808.spec.impl.msg.builder;

import io.github.hylexus.jt.annotation.UnstableApi;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.utils.Assertions;
import io.github.hylexus.oaks.utils.Numbers;
import io.netty.buffer.ByteBufAllocator;

import static io.github.hylexus.jt.utils.Assertions.requireNonNull;

@UnstableApi
public abstract class AbstractJt808MsgBuilder<B, S extends AbstractJt808MsgBuilder<B, S>> implements Jt808MsgBuilder<B, S> {
    protected final Jt808FlowIdGenerator flowIdGenerator;
    protected final Jt808MsgEncoder encoder;

    protected Jt808ProtocolVersion version;
    protected String terminalId;
    protected Integer msgId;
    protected Integer maxPackageSize;
    protected Byte reversedBit15InHeader;
    protected Integer encryptionType = Jt808Response.DEFAULT_ENCRYPTION_TYPE;
    protected B body;

    public AbstractJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder) {
        this.flowIdGenerator = flowIdGenerator;
        this.encoder = encoder;
    }

    public AbstractJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator) {
        this(flowIdGenerator, ByteBufAllocator.DEFAULT);
    }

    public AbstractJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBufAllocator allocator) {
        this(flowIdGenerator, allocator, new DefaultJt808MsgBytesProcessor(allocator));
    }

    public AbstractJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBufAllocator allocator, Jt808MsgBytesProcessor msgBytesProcessor) {
        this.flowIdGenerator = flowIdGenerator;
        this.encoder = new DefaultJt808MsgEncoder(
                allocator,
                msgBytesProcessor,
                responseSubPackage -> {
                },
                Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
                Jt808MsgEncryptionHandler.NO_OPS
        );
    }

    protected abstract Jt808Response toJt808Response();

    @SuppressWarnings("unchecked")
    private S self() {
        return (S) this;
    }

    @Override
    public S body(B body) {
        this.body = body;
        return self();
    }

    @Override
    public B body() {
        return this.body;
    }

    @Override
    public S version(Jt808ProtocolVersion version) {
        this.version = requireNonNull(version, "version() can not be null");
        return self();
    }

    @Override
    public S terminalId(String terminalId) {
        this.terminalId = requireNonNull(terminalId, "terminalId() can not be null");
        return self();
    }

    @Override
    public S encryptionType(int encType) {
        this.encryptionType = encType;
        return self();
    }

    @Override
    public S msgId(int msgId) {
        this.msgId = Assertions.assertThat(msgId, Numbers::isPositive, "msg > 0");
        return self();
    }

    @Override
    public S maxPackageSize(int maxPackageSize) {
        this.maxPackageSize = maxPackageSize;
        return self();
    }

    @Override
    public S reversedBit15InHeader(byte reversedBit15InHeader) {
        this.reversedBit15InHeader = reversedBit15InHeader;
        return self();
    }

}
