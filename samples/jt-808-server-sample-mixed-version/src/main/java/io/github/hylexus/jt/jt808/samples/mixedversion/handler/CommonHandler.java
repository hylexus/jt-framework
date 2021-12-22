package io.github.hylexus.jt.jt808.samples.mixedversion.handler;

import io.github.hylexus.jt.jt808.samples.mixedversion.entity.req.*;
import io.github.hylexus.jt.jt808.samples.mixedversion.entity.resp.ServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.samples.mixedversion.entity.resp.TerminalRegisterReplyMsg;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2013;
import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;

/**
 * @author hylexus
 */
@Slf4j
@Component
@Jt808RequestHandler
public class CommonHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2013)
    public TerminalRegisterReplyMsg processTerminalRegisterMsgV2011(Jt808Request request, TerminalRegisterMsgV2011 body) {

        log.info("V2011--TerminalRegister : {}", body);
        return new TerminalRegisterReplyMsg()
                .setFlowId(request.flowId())
                .setResult((byte) 0)
                .setAuthCode("authCode2011-admin")
                ;
    }

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public TerminalRegisterReplyMsg processTerminalRegisterMsgV2019(Jt808RequestEntity<TerminalRegisterMsgV2019> request) {

        log.info("V2019--TerminalRegister : {}", request);
        return new TerminalRegisterReplyMsg()
                .setFlowId(request.flowId())
                .setResult((byte) 0)
                .setAuthCode("authCode2019-admin")
                ;
    }

    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = VERSION_2013)
    public ServerCommonReplyMsg authMsgV2011(Jt808Request request, TerminalAuthMsgV2011 body) {
        log.info("V2011--auth : {}", body);
        return new ServerCommonReplyMsg()
                .setReplyFlowId(request.flowId())
                .setReplyMsgId(request.msgType().getMsgId())
                .setResult(0);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = VERSION_2019)
    public ServerCommonReplyMsg authMsgV2019(Jt808Request request, TerminalAuthMsgV2019 body) {
        log.info("V2019--auth : {}", body);
        return new ServerCommonReplyMsg()
                .setReplyFlowId(request.flowId())
                .setReplyMsgId(request.msgType().getMsgId())
                .setResult(0);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0200, versions = VERSION_2013)
    public ServerCommonReplyMsg processLocationMsgV2011(Jt808Request request, LocationInfoUploadMsgV2011 body) {
        log.info("V2011--LocationUpload : {}", body);
        return new ServerCommonReplyMsg()
                .setReplyFlowId(request.flowId())
                .setReplyMsgId(request.msgType().getMsgId())
                .setResult(0);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0200, versions = VERSION_2019)
    public ServerCommonReplyMsg processLocationMsgV2019(Jt808Request request, LocationInfoUploadMsgV2019 body) {
        log.info("V2019--LocationUpload : {}", body);
        return new ServerCommonReplyMsg()
                .setReplyFlowId(request.flowId())
                .setReplyMsgId(request.msgType().getMsgId())
                .setResult(0);
    }
}
