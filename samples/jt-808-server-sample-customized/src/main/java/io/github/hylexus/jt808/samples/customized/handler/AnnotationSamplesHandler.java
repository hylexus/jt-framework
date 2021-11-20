package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandlerMapping;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgV2011;
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

    @Jt808RequestMsgHandlerMapping(msgType = 0x0102, desc = "鉴权", versions = Jt808ProtocolVersion.VERSION_2011)
    public CommonReplyMsgBody processAuthMsg(BuiltinAuthRequestMsgV2011 body, Jt808Session session) {
        log.info("Jt808Session 实现类 {}", session.getClass());
        assert session instanceof MySession;
        return CommonReplyMsgBody.success(session.getCurrentFlowId(), Jt808MsgType.CLIENT_AUTH);
    }
}
