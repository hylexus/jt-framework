package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.annotation.UnstableApi;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.ByteBufJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.EntityJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
@UnstableApi
public interface Jt808MsgBuilder<B, S extends Jt808MsgBuilder<B, S>> {

    static ByteBufJt808MsgBuilder newByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBuf body) {
        return new ByteBufJt808MsgBuilder(flowIdGenerator, body);
    }

    static ByteBufJt808MsgBuilder newByteBufBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder, ByteBuf body) {
        return new ByteBufJt808MsgBuilder(flowIdGenerator, encoder, body);
    }

    static EntityJt808MsgBuilder newEntityBuilder(Jt808FlowIdGenerator flowIdGenerator) {
        return new EntityJt808MsgBuilder(flowIdGenerator);
    }

    static EntityJt808MsgBuilder newEntityBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder) {
        return new EntityJt808MsgBuilder(flowIdGenerator, encoder);
    }

    S version(Jt808ProtocolVersion version);

    S msgId(int msgId);

    default S msgId(MsgType msgType) {
        return this.msgId(msgType.getMsgId());
    }

    S terminalId(String terminalId);

    /**
     * @since 2.1.4
     */
    S encryptionType(int encType);

    /**
     * @since 2.1.4
     */
    default S encryptionType(Jt808MsgEncryptionType encType) {
        return this.encryptionType(encType.intValue());
    }

    S body(B body);

    B body();

    S maxPackageSize(int maxPackageSize);

    S reversedBit15InHeader(byte reversedBit15InHeader);

    S release();

    ByteBuf build();

    default String toHexString() {
        ByteBuf byteBuf = null;
        try {
            byteBuf = this.build();
            return HexStringUtils.byteBufToString(byteBuf);
        } catch (Exception e) {
            if (byteBuf != null) {
                JtProtocolUtils.release(byteBuf);
            }
            throw e;
        }
    }
}
