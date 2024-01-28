package io.github.hylexus.jt.demos.jt808.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.hylexus.jt.demos.jt808.service.AttachmentFileService;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1210Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1211Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1212Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg30316364Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg9212Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.extension.attachment.impl.SimpleAttachmentJt808RequestProcessor;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@Jt808RequestHandler
public class AttachmentFileHandler {

    private final AttachmentFileService attachmentFileService;
    // <terminalId, <fileName, AttachmentItem>>
    // 只是个示例而已 看你需求自己改造
    private final Cache<String, Map<String, BuiltinMsg1210Alias.AttachmentItem>> attachmentItemCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    public AttachmentFileHandler(AttachmentFileService attachmentFileService) {
        this.attachmentFileService = attachmentFileService;
    }

    @Jt808RequestHandlerMapping(msgType = 0x1210, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1210(Jt808Request request, BuiltinMsg1210Alias body) {
        log.info("0x1210 ==> {} {}", body.getAttachmentItemList().size(), body);
        warnLogIfNecessary(request, "0x1210 不应该由指令服务器对应的端口处理");
        final Map<String, BuiltinMsg1210Alias.AttachmentItem> itemMap = this.attachmentItemCache.get(request.session().terminalId(), key -> new HashMap<>());
        for (final BuiltinMsg1210Alias.AttachmentItem item : body.getAttachmentItemList()) {
            itemMap.put(item.getFileName().trim(), item.setGroup(body));
        }
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1211, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinServerCommonReplyMsg processMsg0x1211(Jt808Request request, BuiltinMsg1211Alias body) {
        log.info("0x1211 ==> {}", body);
        warnLogIfNecessary(request, "0x1211 不应该由指令服务器对应的端口处理");
        return BuiltinServerCommonReplyMsg.success(request.header().msgId(), request.flowId());
    }

    @Jt808RequestHandlerMapping(msgType = 0x1212, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public BuiltinMsg9212Alias processMsg0x1212(Jt808Request request, BuiltinMsg1212Alias reqBody) {
        log.info("0x1211 ==> {}", reqBody);
        warnLogIfNecessary(request, "0x1212 不应该由指令服务器对应的端口处理");

        final BuiltinMsg9212Alias resp = new BuiltinMsg9212Alias();
        resp.setFileNameLength(reqBody.getFileNameLength());
        resp.setFileName(reqBody.getFileName());
        resp.setFileType(reqBody.getFileType());
        // 0x00：完成
        // 0x01：需要补传
        resp.setUploadResult((byte) 0x00);
        resp.setPackageCountToReTransmit((short) 0);
        resp.setRetransmitItemList(Collections.emptyList());

        return resp;
    }


    /**
     * 这里对应的是苏标附件上传的码流: 0x31326364(并不是 1078 协议中的码流)
     * <p>
     * 在 {@link SimpleAttachmentJt808RequestProcessor#simulateJt808Request(ByteBuf, Jt808Session)} 中将苏标码流上传的报文模拟成了普通的 808 报文
     *
     * @see SimpleAttachmentJt808RequestProcessor#simulateJt808Request(ByteBuf, Jt808Session)
     */
    // 这里是对应的文件上传的码流
    @Jt808RequestHandlerMapping(msgType = 0x30316364)
    public void processMsg30316364(Jt808Request request, BuiltinMsg30316364Alias body, @Nullable Jt808Session session) {
        // 这里的示例都是随便瞎写的 存储到本地磁盘了
        // 实际场景中看你自己需求  比如存储到 OSS、AWS、Minio……
        if (request.session() == null || session == null) {
            log.warn("session == null, 附件上传之前没有没有发送 0x1210,0x1211 消息???");
            return;
        }

        log.info("0x30316364 ==> {} -- {} -- {}", body.getFileName().trim(), body.getDataOffset(), body.getDataLength());

        // 这个 if 就是用来调试的 没啥其他作用  删掉就行
        if (request.session() != session) {
            throw new IllegalStateException("代码不应该执行到这个分支");
        }

        Optional.ofNullable(attachmentItemCache.getIfPresent(session.terminalId())).ifPresent((Map<String, BuiltinMsg1210Alias.AttachmentItem> itemMap) -> {
            final BuiltinMsg1210Alias.AttachmentItem item = itemMap.get(body.getFileName().trim());
            if (item == null) {
                log.error("收到未知附件上传消息: {}", body);
                return;
            }
            this.attachmentFileService.writeDataFragment(session, body, item.getGroup());
        });
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
