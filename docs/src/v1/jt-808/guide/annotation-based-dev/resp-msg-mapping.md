---
headerDepth: 3
---

# 响应消息映射

本节内容是 [基于注解的消息处理器](src/v1/jt-808/guide/annotation-based-dev/msg-handler-register.md) 的后续内容。

所以本小节所说的 `基于注解的响应消息映射` 都是基于 [基于注解的消息处理器](src/v1/jt-808/guide/annotation-based-dev/msg-handler-register.md) 而言的，像下面这种处理方式：

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

`MsgHandler` 除了直接返回 `RespMsgBody` 外，还可以返回 被 `@Jt808RespMsgBody` 标记的实体类。

### 示例代码

- 以下为终端通用应答的示例代码：

```java
@Value
// MsgId 0x8001
@Jt808RespMsgBody(respMsgId = 0x8001)
public class ServerCommonReplyMsgBody {
    // 1. [0-1] 应答流水号 WORD terminal flowId
    @CommandField(order = 0, targetMsgDataType = WORD)
    int replyFlowId;
    // 2. [2-3] 应答id WORD 0x0102 ... 
    @CommandField(order = 1, targetMsgDataType = WORD)
    int replyMsgId;
    // 3. [4] 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持 
    @CommandField(order = 2, targetMsgDataType = BYTE)
    byte result;
}
```
- 以下为对应的Handler返回

```java
@Slf4j
@Jt808RequestMsgHandler
public class CommonHandler {

    @Jt808RequestMsgHandlerMapping(msgType = 0x0200)
    public ServerCommonReplyMsgBody processLocationMsg(
            Session session, RequestMsgMetadata metadata,
            RequestMsgHeader header, LocationUploadRequestMsgBody msgBody) {

        assert header.getMsgId() == BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId();
        assert session.getTerminalId().equals(header.getTerminalId());
        assert session.getTerminalId().equals(metadata.getHeader().getTerminalId());
        assert metadata.getHeader() == header;

        log.info("处理位置上报消息 terminalId = {}, msgBody = {}", header.getTerminalId(), msgBody);
//         return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
        return new ServerCommonReplyMsgBody(header.getFlowId(), CLIENT_LOCATION_INFO_UPLOAD.getMsgId(), (byte) 0);
    }
}
```

### 属性解释

| 属性        | 解释                     | 取值示例 |
| ----------- | ------------------------ | -------- |
| `respMsgId` | 服务端下发消息的 `MsgId` | `0x8001` |
| `desc`      | 描述                     |          |

### @CommandField

| 属性                             | 解释                                                         | 取值示例        |
| -------------------------------- | ------------------------------------------------------------ | --------------- |
| `order`                          | 字节顺序，值越小越先编码                                     | `-1`、`0`、`2`  |
| `targetMsgDataType`              | 数据类型                                                     | `DWORD`         |
| `isNestedCommandField`           | 是否是嵌套类型，`true` 表示被修饰的字段类型是一个 `@CommandField` 嵌套的类型 | 默认值: `false` |
| `customerDataTypeConverterClass` | 客户自定义给字段的编码实现                                   |                 |

