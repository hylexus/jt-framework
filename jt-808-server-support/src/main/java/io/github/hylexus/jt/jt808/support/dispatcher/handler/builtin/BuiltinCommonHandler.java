package io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0002;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2013;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0704V2013;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinTerminalCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j
@Jt808RequestHandler
@BuiltinComponent
public class BuiltinCommonHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0002)
    public BuiltinServerCommonReplyMsg processTerminalHeartBeatMsg(Jt808RequestEntity<BuiltinMsg0002> request) {
        log.info("TerminalHeartBeatMsg, terminalId={}, flowId={}, role={}", request.terminalId(), request.flowId(), request.session().role());
        return BuiltinServerCommonReplyMsg.success(request.msgType().getMsgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x0001)
    public void processCommonReply(Jt808RequestEntity<BuiltinTerminalCommonReplyMsg> requestEntity) {
        log.info("BuiltinTerminalCommonReplyMsg, terminalId={} {}", requestEntity.terminalId(), requestEntity.body());
    }

    @Jt808RequestHandlerMapping(msgType = 0x0200)
    public BuiltinServerCommonReplyMsg processLocationInfoUpload(Jt808RequestEntity<BuiltinMsg0200V2013> requestEntity) {
        log.info("BuiltinLocationInfoUploadMsgV2013 : {}", requestEntity.body());
        return BuiltinServerCommonReplyMsg.success(requestEntity.msgType().getMsgId(), requestEntity.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x0704)
    public BuiltinServerCommonReplyMsg processMsg0704(Jt808RequestEntity<BuiltinMsg0704V2013> requestEntity) {
        log.info("BuiltinMsg0704 : {}", requestEntity.body());
        return BuiltinServerCommonReplyMsg.success(requestEntity.msgType().getMsgId(), requestEntity.flowId());
    }
}
