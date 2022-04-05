package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.MsgType;
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
public class TerminalRegisterMsgHandlerV2013 implements SimpleJt808RequestHandler<Jt808Response> {

    /**
     * 处理 [终端注册] 消息
     */
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Set.of(BuiltinJt808MsgType.CLIENT_REGISTER);
    }

    /**
     * 处理 [V2013] 版的消息
     */
    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2013();
    }

    // 7E0100002F013912344323007B000B0002696431323374797065313233343536373838373635343332314944313233343501B8CA4A2D3132333435332D7E
    @Override
    public Jt808Response handleMsg(Jt808ServerExchange exchange) {
        exchange.request()
                .bodyAsReader()
                // 1. [0-2) WORD 省域ID
                .readUnsignedWord(provinceId -> log.info("省域ID:{}", provinceId))
                // 2. [2-4) WORD 省域ID
                .readUnsignedWord(cityId -> log.info("省域ID:{}", cityId))
                // 3. [4-9) BYTE[5] 制造商ID
                .readString(5, manufacturerId -> log.info("制造商ID:{}", manufacturerId))
                // 4. [9-29) BYTE[20] 终端型号
                .readString(20, terminalType -> log.info("终端型号:{}", terminalType))
                // 5. [29-36) BYTE[7] 终端ID
                .readString(7, terminalId -> log.info("终端ID:{}", terminalId))
                // 6. [36]   BYTE    车牌颜色
                .readByte(color -> log.info("车牌颜色:{}", color))
                // 7. [37,n)   String    车辆标识
                .readString(exchange.request().msgBodyLength() - 37, carIdentifier -> log.info("车辆标识:{}", carIdentifier));

        return Jt808Response.newBuilder()
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                .terminalId(exchange.request().terminalId())
                .flowId(exchange.session().nextFlowId())
                .version(exchange.request().version())
                // 这里演示手动写入数据
                .body(writer -> writer
                        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
                        .writeWord(exchange.request().flowId())
                        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
                        .writeByte(0)
                        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
                        .writeString("AuthCode-123")
                )
                .build();
    }
}
