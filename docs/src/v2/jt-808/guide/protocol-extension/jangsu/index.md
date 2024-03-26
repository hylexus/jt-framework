---
icon: proposal
---

# 苏标扩展(v2.1.4)

::: tip

苏标扩展协议是 <Badge text="2.1.4" type="tip" vertical="middle"/> 中开始支持的。

:::

## 配置项

参考 [配置-苏标扩展](../../../config/extension-jiangsu.md) 。

## 附件服务器

下面是对几个特殊扩展消息 `0x1210`、`0x1211`、`0x1212`、`0x30316364` 的示例性处理流程：

```java

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
```

下面是示例性的文件处理流程：

::: tip

这里只是示例性的将文件存储到了本地磁盘中。

实际使用时你应该将文件存储到其他存储介质，比如 OSS、AWS、Minio 等。

:::

```java

@Service
public class AttachmentFileService {

    private final Jt808AppProps appProps;

    public AttachmentFileService(Jt808AppProps appProps) {
        this.appProps = appProps;
    }

    public void writeDataFragment(Jt808Session session, BuiltinMsg30316364Alias body, BuiltinMsg1210Alias group) {
        final AlarmIdentifierAlias alarmIdentifier = group.getAlarmIdentifier();
        final LocalDateTime localDateTime = alarmIdentifier.getTime();
        // 这里就瞎写了一个路径  看你需求随便改
        final String filePath = appProps.getAttachment().getTemporaryPath() + File.separator
                + DateTimeFormatter.ofPattern("yyyyMMddHH").format(localDateTime) + File.separator
                + session.terminalId() + File.separator
                + group.getMessageType() + File.separator
                // + group.getAlarmNo() + File.separator
                + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(localDateTime) + "-" + group.getAlarmNo() + File.separator
                + body.getFileName().trim();

        final File tempFile = new File(filePath);
        if (!tempFile.exists() && !tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdirs()) {
                throw new RuntimeException("新建文件失败:" + tempFile);
            }
        }
        try (final RandomAccessFile file = new RandomAccessFile(filePath, "rws")) {
            file.seek(body.getDataOffset());
            file.write(body.getData(), 0, (int) body.getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
```
