package io.github.hylexus.jt.jt808.samples.debug.handler;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1210Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1211Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1212Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg30316364Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Jt808RequestHandler
public class AttachmentFileHandler {

    public AttachmentFileHandler() {
    }

    @Jt808RequestHandlerMapping(msgType = 0x1210, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1210(Jt808Request request, BuiltinMsg1210Alias body) {
        log.info("0x1210 ==> {}", body);
        warnLogIfNecessary(request, "0x1210 不应该由指令服务器对应的端口处理");
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1211, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1211(Jt808Request request, BuiltinMsg1211Alias body) {
        log.info("0x1211 ==> {}", body);
        warnLogIfNecessary(request, "0x1211 不应该由指令服务器对应的端口处理");
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1212, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1212(Jt808Request request, BuiltinMsg1212Alias reqBody) {
        log.info("0x1211 ==> {}", reqBody);
        warnLogIfNecessary(request, "0x1212 不应该由指令服务器对应的端口处理");
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
        if (request.session() == null) {
            log.warn("session == null, 附件上传之前没有没有发送 0x1210,0x1211 消息???");
        }
        log.info("0x30316364 ==> session:{}, msg:{}", session, body);
    }

    private void warnLogIfNecessary(Jt808Request request, String msg) {
        if (request.session() == null) {
            return;
        }
        if (request.session().role() == Jt808Session.Role.INSTRUCTION) {
            log.warn(msg);
        }
    }

}
