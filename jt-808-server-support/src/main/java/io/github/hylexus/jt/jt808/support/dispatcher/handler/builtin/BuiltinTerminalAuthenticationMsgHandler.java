package io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin;

import io.github.hylexus.jt.core.BuiltinComponent;
import io.github.hylexus.jt.core.ReplaceableComponent;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0102V2013;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0102V2019;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.*;

/**
 * @author hylexus
 */
@Slf4j
@Jt808RequestHandler
public class BuiltinTerminalAuthenticationMsgHandler implements ReplaceableComponent, BuiltinComponent {

    // 7E0102000C013912344321007B61646D696E2D3132333435364D7E
    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = {VERSION_2013, VERSION_2011})
    public BuiltinServerCommonReplyMsg authMsgV2013(Jt808Request request, BuiltinMsg0102V2013 body) {
        log.info("V2013--auth : {}", body);
        return new BuiltinServerCommonReplyMsg()
                .setClientFlowId(request.flowId())
                .setClientMsgType(request.msgType().getMsgId())
                .setResult(0);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = VERSION_2019)
    public BuiltinServerCommonReplyMsg authMsgV2019(Jt808Request request, BuiltinMsg0102V2019 body) {
        log.info("V2019--auth : {}", body);
        return new BuiltinServerCommonReplyMsg()
                .setClientFlowId(request.flowId())
                .setClientMsgType(request.msgType().getMsgId())
                .setResult(0);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
