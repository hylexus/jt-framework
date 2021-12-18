package io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin;

import io.github.hylexus.jt.core.BuiltinComponent;
import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinTerminalAuthenticationMsgV2011;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinTerminalAuthenticationMsgV2019;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2011;
import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;

/**
 * @author hylexus
 */
@Slf4j
@Jt808RequestHandler
public class BuiltinTerminalAuthenticationMsgHandler implements ReplaceableComponent, BuiltinComponent {

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = VERSION_2011)
    public BuiltinServerCommonReplyMsg authMsgV2011(Jt808Request request, BuiltinTerminalAuthenticationMsgV2011 body) {
        log.info("V2011--auth : {}", body);
        return new BuiltinServerCommonReplyMsg()
                .setTerminalFlowId(request.flowId())
                .setTerminalMsgId(request.msgType().getMsgId())
                .setResult(0);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = VERSION_2019)
    public BuiltinServerCommonReplyMsg authMsgV2019(Jt808Request request, BuiltinTerminalAuthenticationMsgV2019 body) {
        log.info("V2019--auth : {}", body);
        return new BuiltinServerCommonReplyMsg()
                .setTerminalFlowId(request.flowId())
                .setTerminalMsgId(request.msgType().getMsgId())
                .setResult(0);
    }

}
