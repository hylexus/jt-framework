# 响应消息映射

本节内容是 [基于注解的消息处理器](./msg-handler-register.md) 的后续内容。

所以本小节所说的 `基于注解的响应消息映射` 都是基于 [基于注解的消息处理器](./msg-handler-register.md) 而言的，像下面这种处理方式：

```java
@Slf4j
@Jt808RequestMsgHandler
public class CommonHandler {

    @Jt808RequestMsgHandlerMapping(msgType = 0x0102)
    public RespMsgBody processAuthMsg(AuthRequestMsgBody msgBody, RequestMsgHeader header) {
        log.info("处理鉴权消息 terminalId = {}, authCode = {}", header.getTerminalId(), msgBody.getAuthCode());
        return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_AUTH);
    }
}
```

## 直接返回RespMsgBody

对于这种直接返回 `RespMsgBody` 的 `MsgHandler`，其实转换逻辑已经在 `RespMsgBody.toBytes()` 里实现了。

常用的 `服务端通用应答消息`，可以用内置的 `CommonReplyMsgBody` 来表示。此处不再赘述。

```java
@Data
@Accessors(chain = true)
public class CommonReplyMsgBody implements RespMsgBody {

    // 1. 应答流水号 WORD terminal flowId
    private int replyFlowId;
    // 2. 应答id WORD 0x0102 ...
    private int replyMsgId;
    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    private final byte result = SUCCESS;

    private CommonReplyMsgBody() {
    }

    public static CommonReplyMsgBody success(int replyFlowId, MsgType replyFor) {
        return of(SUCCESS, replyFlowId, replyFor);
    }

    public static CommonReplyMsgBody of(byte result, int replyFlowId, MsgType replyFor) {
        return new CommonReplyMsgBody()
                .setResult(result)
                .setReplyFlowId(replyFlowId)
                .setReplyMsgId(replyFor.getMsgId());
    }

    @Override
    public byte[] toBytes() {
        return Bytes.concatAll(
                IntBitOps.intTo2Bytes(this.getReplyFlowId()),
                IntBitOps.intTo2Bytes(this.getReplyMsgId()),
                new byte[]{this.getResult()}
        );
    }

    @Override
    public MsgType replyMsgType() {
        return BuiltinJt808MsgType.SERVER_COMMON_REPLY;
    }
}
```

## 不给客户端回复数据

::: tip 以下情况不会发送数据给客户端：
- 手动实现的 `MsgHandler` 
    - 返回 `Optional.empty()`
- 基于 `@Jt808RequestMsgHandlerMapping` 实现的 `MsgHandler`
    - 方法返回类型为 `void`
    - 方法返回值为 `VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT`
:::

## @Jt808RespMsgBody

`MsgHandler` 除了直接返回 `RespMsgBody` 外，还以返回 被 `@Jt808RespMsgBody` 标记的实体类。

::: tip TODO
这部分可用的注解和其解析逻辑还在开发中……😂😂😂😂😂😂😂😂
:::
