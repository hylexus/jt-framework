package io.github.hylexus.jt.jt808.spec.impl.msg.builder;

import io.github.hylexus.jt.annotation.UnstableApi;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteWriter;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.function.Consumer;

import static io.github.hylexus.jt.utils.Assertions.assertThat;
import static io.github.hylexus.jt.utils.Assertions.requireNonNull;

/**
 * @author hylexus
 */
@UnstableApi
public class ByteBufJt808MsgBuilder extends AbstractJt808MsgBuilder<ByteBuf, ByteBufJt808MsgBuilder> {

    public ByteBufJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder, ByteBuf byteBuf) {
        super(flowIdGenerator, encoder);
        this.body = byteBuf;
    }

    public ByteBufJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBuf byteBuf) {
        super(flowIdGenerator);
        this.body = byteBuf;
    }

    public ByteBufJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBufAllocator allocator, ByteBuf byteBuf) {
        super(flowIdGenerator, allocator);
        this.body = byteBuf;
    }

    public ByteBufJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBufAllocator allocator, Jt808MsgBytesProcessor msgBytesProcessor, ByteBuf byteBuf) {
        super(flowIdGenerator, allocator, msgBytesProcessor);
        this.body = byteBuf;
    }

    @Override
    protected Jt808Response toJt808Response() {
        return new DefaultJt808Response(
                requireNonNull(version, "version() can not be null"),
                assertThat(msgId, id -> id != null && id > 0, "msgId() can not be null"),
                encryptionType,
                maxPackageSize == null ? Jt808Response.DEFAULT_MAX_PACKAGE_SIZE : maxPackageSize,
                reversedBit15InHeader == null ? 0 : reversedBit15InHeader,
                requireNonNull(terminalId, "terminalId() can not be null"),
                -1,
                requireNonNull(body, "body() can not be null")
        );
    }

    public ByteBufJt808MsgBuilder body(Consumer<Jt808ByteWriter> consumer) {
        final Jt808ByteWriter writer = Jt808ByteWriter.of(body());
        consumer.accept(writer);
        return super.body(body);
    }

    @Override
    public ByteBufJt808MsgBuilder body(ByteBuf body) {
        final ByteBuf old = this.body();
        try {
            return super.body(body);
        } finally {
            JtProtocolUtils.release(old);
        }
    }

    @Override
    public ByteBuf build() {
        Jt808Response jt808Response = null;
        try {
            jt808Response = this.toJt808Response();
            return this.encoder.encode(jt808Response, this.flowIdGenerator);
        } catch (Exception e) {
            this.release();
            if (jt808Response != null) {
                jt808Response.release();
            }
            throw e;
        }
    }

    @Override
    public ByteBufJt808MsgBuilder release() {
        JtProtocolUtils.release(this.body);
        return this;
    }
}
