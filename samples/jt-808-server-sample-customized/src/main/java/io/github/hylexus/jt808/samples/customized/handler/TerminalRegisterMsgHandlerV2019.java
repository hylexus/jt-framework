package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0100V2019;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg8100;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.SimpleJt808RequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author hylexus
 */
@Slf4j
@Component
public class TerminalRegisterMsgHandlerV2019 implements SimpleJt808RequestHandler<BuiltinMsg8100> {

    /**
     * 处理 [终端注册] 消息
     */
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Set.of(BuiltinJt808MsgType.CLIENT_REGISTER);
    }

    /**
     * 处理 [V2019] 版的消息
     */
    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2019();
    }

    // 7E010040560100000000013912344329007B000B0002696439383736353433323174797065303031323334353637383132333
    // 435363738383736353433323149443030303031323334353637383132333435363738383736353433323101B8CA4A2D313233343539257E
    @Override
    public BuiltinMsg8100 handleMsg(Jt808ServerExchange exchange) {
        final BuiltinMsg0100V2019 body = new BuiltinMsg0100V2019();
        exchange.request()
                .bodyAsReader()
                // 1. [0-2) WORD 省域ID
                .readUnsignedWord(body::setProvinceId)
                // 2. [2-4) WORD 省域ID
                .readUnsignedWord(body::setCityId)
                // 3. [4-15) BYTE[11] 制造商ID
                .readString(11, body::setManufacturerId)
                // 4. [15-45) BYTE[30] 终端型号
                .readString(30, body::setTerminalType)
                // 5. [45-75) BYTE[30] 终端ID
                .readString(30, body::setTerminalId)
                // 6. [75]   BYTE    车牌颜色
                .readByte(body::setColor)
                // 7. [76,n)   String    车辆标识
                .readString(exchange.request().msgBodyLength() - 76, body::setCarIdentifier);
        log.info("body:{}", body);

        return new BuiltinMsg8100()
                .setTerminalFlowId(exchange.request().flowId())
                .setAuthCode("AuthCode-admin-2019")
                .setResult((byte) 0);

    }
}
