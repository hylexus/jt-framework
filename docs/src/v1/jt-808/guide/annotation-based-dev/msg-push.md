---
headerDepth: 3
---

# 消息下发

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-annotation) 下找到对应代码。
:::

::: tip
本小节将介绍如何主动下发消息给终端。
:::

## 1. 手动下发

```java
@Component
public class SomeController {
    
    @Autowired
    private Jt808SessionManager sessionManager;

    public void sendDataToClient(String terminalId) {
        // 通过终端id获取Session
        final Optional<Jt808Session> sessionInfo = sessionManager.findByTerminalId(terminalId);
        if (!sessionInfo.isPresent()) {
            throw new JtSessionNotFoundException("session not found with terminalId " + terminalId);
        }
        try {
            // 通过Session发送数据
            sessionInfo.get().sendMsgToClient(Unpooled.copiedBuffer("data will be sent to client".getBytes()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

::: danger
- 手动发送的数据，需要手动拼装为符合808协议格式的报文，并手动转义。
- 所以内置了一个CommandSender来自动转换为符合808协议格式的报文并发送给客户端。
:::

## 2. 使用CommandSender下发

### 实现原理

TODO 这里缺一个图，暂时没时间画了…………

### 示例

```java
@Slf4j
@RestController
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private CommandSender commandSender;

    @GetMapping("/send-msg")
    public Object sendMsg(
            @RequestParam(required = false, name = "terminalId", defaultValue = "13717861955") String terminalId,
            @RequestParam(required = false, name = "timeout", defaultValue = "5") Long timeout) throws Exception {

        RespTerminalSettings param = new RespTerminalSettings();
        List<RespTerminalSettings.ParamItem> paramList = Lists.newArrayList(
                new RespTerminalSettings.ParamItem(0x0029, DwordBytesValueWrapper.of(100))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        // 【下发消息】的消息类型为: RESP_TERMINAL_PARAM_SETTINGS (0x8103)  --> RespTerminalSettings的类注解上指定了下发类型
        // 客户端对该【下发消息】的回复消息类型为: CLIENT_COMMON_REPLY (0x0001)
        CommandMsg commandMsg = CommandMsg.of(terminalId, CLIENT_COMMON_REPLY, param);
        final Object resp = commandSender.sendCommandAndWaitingForReply(commandMsg, timeout, TimeUnit.SECONDS);
        log.info("resp: {}", resp);
        return resp;
    }
}
```

```java
@Data
@Accessors(chain = true)
@Jt808RespMsgBody(respMsgId = 0x8103, desc = "设置终端参数")
public class RespTerminalSettings {

    @CommandField(order = 2)
    private List<ParamItem> paramList;

    @CommandField(order = 1, targetMsgDataType = BYTE)
    private int totalParamCount;

    @Data
    @Accessors(chain = true)
    @SuppressWarnings("rawtypes")
    public static class ParamItem {
        @CommandField(order = 1, targetMsgDataType = DWORD)
        private int msgId;

        @CommandField(order = 2, targetMsgDataType = BYTE)
        private int bytesCountOfContentLength;

        @CommandField(order = 3)
        private BytesValueWrapper msgContent;

        public ParamItem(int msgId, BytesValueWrapper msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.bytesCountOfContentLength = msgContent.getAsBytes().length;
        }
    }

}
```

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-annotation) 下找到对应代码。
:::