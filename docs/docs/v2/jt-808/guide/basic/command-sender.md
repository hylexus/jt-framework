# 消息下发

## 手动下发

```java{18-19}
@RestController
@RequestMapping("/demo01")
public class DemoController {

    private final Jt808CommandSender commandSender;
    private final Jt808SessionManager sessionManager;

    public DemoController(Jt808CommandSender commandSender, Jt808SessionManager sessionManager) {
        this.commandSender = commandSender;
        this.sessionManager = sessionManager;
    }

    @RequestMapping("/send-some-msg")
    public void sendMsgBySession(@RequestParam(name = "terminalId", required = false, defaultValue = "013912344323") String terminalId) {

        this.sessionManager.findByTerminalId(terminalId)
                .orElseThrow(() -> new IllegalArgumentException("No terminal found with terminalId " + terminalId))
                // 手动编码后通过 Session 下发消息
                .sendMsgToClient(ByteBufAllocator.DEFAULT.buffer().writeBytes("data will be sent to client".getBytes(Charset.forName("GBK"))));
    }
}
```

## 通过CommandSender下发

下发消息，并等待。

```java{29-32}
@RestController
@RequestMapping("/demo01")
public class DemoController {

    private final Jt808CommandSender commandSender;
    private final Jt808SessionManager sessionManager;

    public DemoController(Jt808CommandSender commandSender, Jt808SessionManager sessionManager) {
        this.commandSender = commandSender;
        this.sessionManager = sessionManager;
    }

    // 7E00010005013912344323007B0001810300827E
    @RequestMapping("/set-terminal-params")
    public Object sendMsg(
            @RequestParam(name = "terminalId", required = false, defaultValue = "013912344323") String terminalId) throws InterruptedException {

        final RespTerminalSettings param = new RespTerminalSettings();
        final List<RespTerminalSettings.ParamItem> paramList = List.of(
                new RespTerminalSettings.ParamItem(0x0029, ByteBufAllocator.DEFAULT.buffer().writeInt(100)),
                new RespTerminalSettings.ParamItem(0x0030, ByteBufAllocator.DEFAULT.buffer().writeInt(211))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        final Jt808Session session = sessionManager.findByTerminalId(terminalId).orElseThrow();
        final int nextFlowId = session.nextFlowId();

        // 1. 生成Key(收到终端回复时会根据这个Key来匹配)
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, nextFlowId);

        final Object resp = commandSender.sendCommandAndWaitingForReply(commandKey, param, 20L, TimeUnit.SECONDS);
        log.info("RESP::::::: {}", resp);
        return resp;
    }
}
```

收到结果后，放入 `CommandWaitingPool`。

```java{10-13}
@Component
@Jt808RequestHandler
public class CommonHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0001, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public void processMsg0001(Jt808RequestEntity<BuiltinTerminalCommonReplyMsg> request) {
        final BuiltinTerminalCommonReplyMsg body = request.body();
        final String terminalId = request.header().terminalId();

        // 2. 生成同样的Key
        final Jt808CommandKey commandKey = Jt808CommandKey.of(terminalId, BuiltinJt808MsgType.CLIENT_COMMON_REPLY, body.getServerFlowId());
        // 将结果放入CommandWaitingPool
        CommandWaitingPool.getInstance().putIfNecessary(commandKey, body);
    }
}
```