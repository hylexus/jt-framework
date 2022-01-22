package io.github.hylexus.jt808.samples.annotation.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.CommandWaitingPool;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0100V2019;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinTerminalCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;

/**
 * @author hylexus
 */
@Slf4j
@Component
@Jt808RequestHandler
public class CommonHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0001, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public void processMsg0001(Jt808RequestEntity<BuiltinTerminalCommonReplyMsg> request) {
        final BuiltinTerminalCommonReplyMsg body = request.body();
        final String terminalId = request.header().terminalId();

        // 2. 生成同样的Key
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, body.getServerFlowId());
        // 将结果放入CommandWaitingPool
        CommandWaitingPool.getInstance().putIfNecessary(commandKey, body);
    }

    // terminalId: 013912344329
    // 7E010040560100000000013912344329007B000B00026964393837363534333231747970653030313233343536373831323334353637383837363534333231494
    // 43030303031323334353637383132333435363738383736353433323101B8CA4A2D313233343539257E
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public TerminalRegisterReplyMsg clientRegisterV2019(Jt808RequestEntity<BuiltinMsg0100V2019> request) {
        final BuiltinMsg0100V2019 body = request.body();
        log.info("client register v2019 : {}", body);
        return new TerminalRegisterReplyMsg().setFlowId(request.flowId()).setResult((byte) 0).setAuthCode("AuthCode2019----");
    }

    @Data
    @Accessors(chain = true)
    // 这里用来测试分包消息(指定单个消息包最大大小为33字节)
    @Jt808ResponseBody(msgId = 0x8100, maxPackageSize = 33)
    public static class TerminalRegisterReplyMsg {
        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
        @ResponseField(order = 0, dataType = MsgDataType.WORD)
        private int flowId;
        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
        @ResponseField(order = 1, dataType = MsgDataType.BYTE)
        private byte result;
        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
        @ResponseField(order = 3, dataType = MsgDataType.STRING, conditionalOn = "result == 0")
        private String authCode;
    }
}
