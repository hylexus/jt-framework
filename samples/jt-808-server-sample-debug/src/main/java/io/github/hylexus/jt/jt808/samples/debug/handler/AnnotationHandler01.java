package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.DebugTerminalRegisterMsgV2013;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.DebugTerminalRegisterMsgV2019;
import io.github.hylexus.jt.jt808.samples.debug.entity.req.TerminalCommonReplyMsg;
import io.github.hylexus.jt.jt808.samples.debug.entity.resp.TerminalRegisterReplyRespMsg;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0005;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0102V2019;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg8003;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;

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

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    public Jt808Response process(Jt808Request request, Jt808Response response) {
        // ... process(request)
        return response.msgId(BuiltinJt808MsgType.SERVER_COMMON_REPLY.getMsgId())
                .writeWord(request.flowId())
                .writeByte(0)
                .writeString("AuthCodeXxx");
    }

    // @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    public Object processRegisterMsgV2013(Jt808Request request, Jt808Session session, DebugTerminalRegisterMsgV2013 authMsgV2013) {
        log.info("{}", authMsgV2013);
        // 测试异常处理
        if (new Random().nextBoolean()) {
            throw new IllegalStateException("Random exception ...");
        }
        return new TerminalRegisterReplyRespMsg()
                .setFlowId(request.flowId())
                .setResult((byte) 0)
                .setAuthCode("AuthCode2013DebugDemo");
    }

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public Jt808Response processRegisterMsgV2019(Jt808Request request, Jt808Session session, DebugTerminalRegisterMsgV2019 authMsgV2019) {
        log.info("{}", authMsgV2019);
        return Jt808Response.newBuilder()
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                .version(VERSION_2019)
                .terminalId(session.terminalId())
                .flowId(session.nextFlowId())
                .body(bodyWriter -> bodyWriter
                        .writeWord(request.flowId())
                        .writeByte(0)
                        .writeString("AuthCode2019DebugDemo")
                ).build()
                ;
    }


    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = VERSION_2019)
    public Object debug001(Jt808RequestEntity<BuiltinMsg0102V2019> request) {
        log.info("{}", request.body());

        return new BuiltinMsg8003()
                .setFirstSubPackageFlowId(101)
                .setTotalCount(3)
                .setPackageIdList(Jdk8Adapter.listOf(new BuiltinMsg8003.PackageId(2), new BuiltinMsg8003.PackageId(4), new BuiltinMsg8003.PackageId(5)));
    }

    @Jt808RequestHandlerMapping(msgType = 0x8003, versions = VERSION_2019)
    public Object debug002(Jt808RequestEntity<BuiltinMsg0005> request) {
        log.info("body: {}", request.body());
        return new BuiltinMsg8003()
                .setFirstSubPackageFlowId(101)
                .setTotalCount(3)
                .setPackageIdList(Jdk8Adapter.listOf(new BuiltinMsg8003.PackageId(2), new BuiltinMsg8003.PackageId(4), new BuiltinMsg8003.PackageId(5)));
    }
}
