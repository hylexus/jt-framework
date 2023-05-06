package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hylexus
 */
@Slf4j
@Component
@Jt808RequestHandler
public class SubPackageDebug {

    @Jt808RequestHandlerMapping(msgType = 0x8100)
    public DemoServerCommonReplyRespMsg debugTerminalRegisterReplyRespMsg(Jt808RequestEntity<TerminalRegisterReplySubPackageDebug> entity) {
        log.info("{}", entity.body());
        return new DemoServerCommonReplyRespMsg().setMsgId(0x8100).setFlowId(1).setResult(0);
    }

    @Data
    public static class TerminalRegisterReplySubPackageDebug {
        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
        @RequestField(order = 0, dataType = MsgDataType.WORD)
        private int flowId;
        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
        @RequestField(order = 1, dataType = MsgDataType.BYTE)
        private byte result;
        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
        @RequestField(order = 3, dataType = MsgDataType.STRING, lengthExpression = "#ctx.msgBodyLength() - 3")
        private String authCode;
    }

    @Jt808ResponseBody(msgId = 0x001)
    @Data
    @Accessors(chain = true)
    public static class DemoServerCommonReplyRespMsg {
        @ResponseField(order = 1, dataType = MsgDataType.WORD)
        private int flowId;

        @ResponseField(order = 1, dataType = MsgDataType.WORD)
        private int msgId;

        @ResponseField(order = 3, dataType = MsgDataType.BYTE)
        private int result;
    }
}
