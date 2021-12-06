package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.response.CommandWaitingPool;
import io.github.hylexus.jt.jt808.response.Jt808CommandKey;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.DebugTerminalRegisterMsgV2011;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.DebugTerminalRegisterMsgV2019;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.TerminalCommonReplyMsg;
import io.github.hylexus.jt.jt808.samples.debug.entity.resp.TerminalRegisterReplyRespMsg;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandlerMapping;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@Jt808RequestMsgHandler
public class AnnotationHandler01 {

    @Jt808RequestMsgHandlerMapping(msgType = 0x0001, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public void processMsg0001(Jt808Request request, TerminalCommonReplyMsg body) {
        final int serverMsgId = body.getServerMsgId();
        final String terminalId = request.header().terminalId();
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, serverMsgId);
        CommandWaitingPool.getInstance().putIfNecessary(commandKey, body);
    }

    @Jt808RequestMsgHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2011)
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

    @Jt808RequestMsgHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2019)
    public Jt808Response processRegisterMsgV2019(Jt808Request request, Jt808Session session, DebugTerminalRegisterMsgV2019 authMsgV2019) {
        log.info("{}", authMsgV2019);
        return Jt808Response.newBuilder()
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                .version(Jt808ProtocolVersion.VERSION_2019)
                .terminalId(session.getTerminalId())
                .flowId(session.getCurrentFlowId())
                .body(Jt808ByteBuf.from(ByteBufAllocator.DEFAULT.buffer())
                        .writeWord(request.flowId())
                        .writeByte(0)
                        .writeString("AuthCode2019DebugDemo")
                ).build()
                ;
    }
}
