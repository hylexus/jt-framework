---
icon: return
---

# 响应消息处理器

## 说明

::: tip

对响应给客户端的消息类型没有强制指定固定类型，任何类型的消息处理器返回的结果都被包装成了 `Jt808HandlerResult`。

而 `Jt808HandlerResult` 是由 `Jt808HandlerResultHandler` 处理的。

所以，支持哪些类型的响应消息取决于 `Jt808HandlerResultHandler` 的配置。

:::

内置了两个 `Jt808HandlerResultHandler`:

<p class="demo">
    <img :src="$withBase('/img/v2/design/jt808-handler-result-handler.png')">
</p>

- `Jt808ResponseHandlerResultHandler` 能处理 `Jt808Response` 类型的响应数据。
- `Jt808ResponseBodyHandlerResultHandler` 能将处理被 `@Jt808ResponseBody` 注解修饰的返回类型。

::: tip

本小节主要介绍 `Jt808ResponseHandlerResultHandler` 类型的 **类级别** 处理器，毕竟当注解不方便处理请求时，这种类型的 **HandlerResultHandler** 是内置的 *
*HandlerResultHandler** 中唯一的选择。

[//]: # (基于注解的请求消息处理，请移步 todo)

:::

## @Jt808ResponseBody

### 说明

这个注解也是借鉴(抄袭)`Spring` 的 `@ResponseBody` 注解，表示被标记的类是响应体。

### 示例

下面是被 `@Jt808ResponseBody` 标记的类，表示该类是给客户端回复数据的 `body()` 部分：

```java{3}
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8100, maxPackageSize = 33)
public class TerminalRegisterReplyMsg {
    // 1. byte[0,2) WORD 对应的终端注册消息的流水号
    @ResponseField(order = 0, dataType = MsgDataType.WORD)
    private int flowId;
    // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
    @ResponseField(order = 1, dataType = MsgDataType.BYTE)
    private byte result;
    // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
    @ResponseField(order = 3, dataType = MsgDataType.STRING, conditionalOn = "result == 0")
    private String authCode;
}
```

下面是回复客户端的部分伪代码：

```java{5}
@Component
@Jt808RequestHandler
public class CommonHandler {
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public TerminalRegisterReplyMsg processTerminalRegisterMsgV2019(Jt808RequestEntity<TerminalRegisterMsgV2019> request) {

        log.info("V2019--TerminalRegister : {}", request);
        return new TerminalRegisterReplyMsg()
                .setFlowId(request.flowId())
                .setResult((byte) 0)
                .setAuthCode("authCode2019-admin")
                ;
    }

}
```

## Jt808Response

### 说明

### 示例

@[code java{23}](@example-src/808/v2/basic/response-processing/Jt808ResponseDemo.java)

