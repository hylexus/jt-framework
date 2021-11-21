package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandlerMapping;
import io.github.hylexus.jt.command.CommandWaitingPool;
import io.github.hylexus.jt.command.Jt808CommandKey;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgBody;
import io.github.hylexus.jt808.msg.req.BuiltinTerminalCommonReplyMsgBody;
import io.github.hylexus.jt808.msg.resp.CommonReplyMsgBody;
import io.github.hylexus.jt808.samples.customized.config.Jt808MsgType;
import io.github.hylexus.jt808.samples.customized.session.MySession;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created At 2020-06-24 15:20
 *
 * @author hylexus
 */
@Slf4j
@Jt808RequestMsgHandler
@Component
public class AnnotationSamplesHandler {

    @Jt808RequestMsgHandlerMapping(msgType = 0x0102, desc = "鉴权")
    public CommonReplyMsgBody processAuthMsg(BuiltinAuthRequestMsgBody body, Jt808Session session) {
        log.info("Jt808Session 实现类 {}", session.getClass());
        assert session instanceof MySession;
        return CommonReplyMsgBody.success(session.getCurrentFlowId(), Jt808MsgType.CLIENT_AUTH);
    }

    @Jt808RequestMsgHandlerMapping(msgType = 0x0001, desc = "终端通用应答")
    public void processBuiltinTerminalCommonReplyMsgBody(BuiltinTerminalCommonReplyMsgBody body, RequestMsgHeader header) {
        log.info("-----> {}", body);
        final Jt808CommandKey commandKey = Jt808CommandKey.of(header.getTerminalId(), Jt808MsgType.CLIENT_COMMON_REPLY, body.getReplyFlowId());
        CommandWaitingPool.getInstance().putIfNecessary(commandKey, body);
    }
}
