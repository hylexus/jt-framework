package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.DebugTerminalRegisterMsgV2011;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.DebugTerminalRegisterMsgV2019;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.TerminalCommonReplyMsg;
import io.github.hylexus.jt.jt808.samples.debug.entity.resp.TerminalRegisterReplyRespMsg;
import io.github.hylexus.jt.jt808.spec.CommandWaitingPool;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@Jt808RequestHandler
public class AnnotationHandler01 {

    @Jt808RequestHandlerMapping(msgType = 0x0001, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public void processMsg0001(Jt808Request request, TerminalCommonReplyMsg body) {
        final int serverMsgId = body.getServerMsgId();
        final String terminalId = request.header().terminalId();
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, serverMsgId);
        CommandWaitingPool.getInstance().putIfNecessary(commandKey, body);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2011)
    public Object processRegisterMsgV2011(Jt808Request request, Jt808Session session, DebugTerminalRegisterMsgV2011 authMsgV2011) {
        log.info("{}", authMsgV2011);
        // 测试异常处理
        if (new Random().nextBoolean()) {
            throw new IllegalStateException("Random exception ...");
        }
        return new TerminalRegisterReplyRespMsg()
                .setFlowId(request.flowId())
                .setResult((byte) 0)
                .setAuthCode("AuthCode2011DebugDemo");
    }

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2019)
    public Jt808Response processRegisterMsgV2019(Jt808Request request, Jt808Session session, DebugTerminalRegisterMsgV2019 authMsgV2019) {
        log.info("{}", authMsgV2019);
        return Jt808Response.newBuilder()
                .msgType(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                .version(Jt808ProtocolVersion.VERSION_2019)
                .terminalId(session.getTerminalId())
                .flowId(session.getCurrentFlowId())
                .body(bodyWriter -> bodyWriter
                        .writeWord(request.flowId())
                        .writeByte(0)
                        .writeString("AuthCode2019DebugDemo")
                ).build()
                ;
    }
    //
    //    @Jt808RequestHandlerMapping(msgType = 0x8100)
    //    public DemoServerCommonReplyRespMsg debugTerminalRegisterReplyRespMsg(Jt808RequestEntity<TerminalRegisterReplySubPackageDebug> entity) {
    //        log.info("{}", entity.body());
    //        return new DemoServerCommonReplyRespMsg().setMsgId(0x8100).setFlowId(1).setResult(0);
    //    }
    //
    //    public static class TerminalRegisterReplySubPackageDebug {
    //        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
    //        @RequestField(order = 0, startIndex = 0, dataType = MsgDataType.WORD)
    //        private int flowId;
    //        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
    //        @RequestField(order = 1, startIndex = 2, dataType = MsgDataType.BYTE)
    //        private byte result;
    //        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
    //        @RequestField(order = 3, startIndex = 3, dataType = MsgDataType.STRING, lengthExpression = "#header.msgBodyLength() - 3")
    //        private String authCode;
    //    }
}
