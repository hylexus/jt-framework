# 消息处理器注册

::: tip 传送门
本小节的示例代码可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-annotation) 下找到对应代码。
:::

## @Jt808RequestMsgHandler

- 该注解类似于 `SpringMVC` 的 `@Controller`。
- `@Jt808RequestMsgHandler` 只能标注于 `类` 上，表示该类中存在 `MsgHandler` 方法。

```java
@Jt808RequestMsgHandler
public class CommonHandler {
    // ...
}
```

## @Jt808RequestMsgMapping

- 该注解类似于 `SpringMVC` 的 `@RequestMapping`。
- `@Jt808RequestMsgMapping` 只能标注于 `方法` 上，表示被注解的方法是一个 `MsgHandler` 方法。
- 从逻辑上来理解，被其注解的方法相当于一个实现了 `MsgHandler` 接口的处理器类。

```java
@Slf4j
@Jt808RequestMsgHandler
public class CommonHandler {
    // 处理MsgId为0x0200的消息
    @Jt808RequestMsgMapping(msgType = 0x0200)
    public RespMsgBody processLocationMsg(
            Session session, RequestMsgMetadata metadata,
            RequestMsgHeader header, LocationUploadMsgBody msgBody) {

        assert header.getMsgId() == BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId();
        assert session.getTerminalId().equals(header.getTerminalId());
        assert session.getTerminalId().equals(metadata.getHeader().getTerminalId());
        assert metadata.getHeader() == header;

        log.info("处理位置上报消息 terminalId = {}, msgBody = {}", header.getTerminalId(), msgBody);
        return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
    }

}
```
::: tip 传送门
本小节的示例代码可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-annotation) 下找到对应代码。
:::
