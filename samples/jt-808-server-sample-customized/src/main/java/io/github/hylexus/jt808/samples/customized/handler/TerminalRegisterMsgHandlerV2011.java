package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg8100Alias;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author hylexus
 */
@Slf4j
@Component
public class TerminalRegisterMsgHandlerV2011 implements SimpleJt808RequestHandler<BuiltinMsg8100Alias> {

    /**
     * 处理 [终端注册] 消息
     */
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Jdk8Adapter.setOf(BuiltinJt808MsgType.CLIENT_REGISTER);
    }

    /**
     * 处理 [V2011] 版的消息
     */
    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2011();
    }

    /**
     * 这里以 {@link ResponseFieldAlias} 注解来作示例
     *
     * @see ResponseFieldAlias
     */
    // 7E01000023013912344321007B000B0002696431323361626364656667684944313233343501B8CA4A2D313233343531317E
    @Override
    public BuiltinMsg8100Alias handleMsg(Jt808ServerExchange exchange) {
        final ByteBuf body = exchange.request().body();
        // 1. [0-2) WORD 省域ID
        final int province = JtProtocolUtils.readUnsignedWord(body);
        // 2. [2-4) WORD 省域ID
        final int cityId = JtProtocolUtils.readUnsignedWord(body);
        // 3. [4-9) BYTE[5] 制造商ID
        final String manufacturerId = JtProtocolUtils.readString(body, 5);
        // 4. [9-17) BYTE[8] 终端型号
        final String terminalType = JtProtocolUtils.readString(body, 8);
        // 5. [17-24) BYTE[7] 终端ID
        final String terminalIdentifier = JtProtocolUtils.readString(body, 7);
        // 6. [24]   BYTE    车牌颜色
        final byte color = body.readByte();
        // 7. [25,n)   String    车辆标识
        final String carIdentifier = JtProtocolUtils.readString(body, exchange.request().msgBodyLength() - 25);
        log.info("terminalId={}, terminalIdentifier={}, carIdentifier={}", exchange.request().terminalId(), terminalIdentifier, carIdentifier);

        return new BuiltinMsg8100Alias()
                .setTerminalFlowId(exchange.request().flowId())
                .setAuthCode("AuthCode-admin-2011")
                .setResult((byte) 0);
    }
}
