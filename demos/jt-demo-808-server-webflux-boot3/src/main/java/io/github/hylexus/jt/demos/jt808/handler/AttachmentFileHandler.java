package io.github.hylexus.jt.demos.jt808.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1210Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1211Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1212Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg30316364Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg9208Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg9212Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Component
@Jt808RequestHandler
public class AttachmentFileHandler {
    private final Jt808SessionManager sessionManager;

    public AttachmentFileHandler(Jt808SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Jt808RequestHandlerMapping(msgType = 0x1210, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1210(Jt808Request request, BuiltinMsg1210Alias body) {
        log.info("0x1210 ==> {}", body);
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1211, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1211(Jt808Request request, BuiltinMsg1211Alias body) {
        log.info("0x1211 ==> {}", body);
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1212, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1212(Jt808Request request, BuiltinMsg1212Alias reqBody) {
        log.info("0x1211 ==> {}", reqBody);
        // final BuiltinMsg9212Alias respBody = new BuiltinMsg9212Alias();
        // respBody.setFileNameLength(reqBody.getFileNameLength());
        // respBody.setFileName(reqBody.getFileName());
        // respBody.setFileType(reqBody.getFileType());
        // respBody.setUploadResult((byte) 0x00);
        // respBody.setPackageCountToReTransmit((short) 0);

        // this.sendMsg9212(request.terminalId(), respBody);
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x30316364)
    public void processMsg30316364(Jt808Request request, BuiltinMsg30316364Alias body, @Nullable Jt808Session session) {
        log.info("0x30316364 ==> session:{}, msg:{}", session, body);
    }

    public void sendMsg9212(String terminalId, BuiltinMsg9212Alias body) {
        final Jt808Session session = this.sessionManager.findByTerminalId(terminalId).orElseThrow();

        final ByteBuf byteBuf = Jt808MsgBuilder.newEntityBuilder(session)
                .version(session.protocolVersion())
                .terminalId(terminalId)
                .body(body)
                .build();
        session.sendMsgToClient(byteBuf);
    }
}
