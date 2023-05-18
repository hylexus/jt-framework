---
icon: at
---

# 响应消息映射

## @Jt808ResponseBody

该注解的作用类似于 `WebFlux/WebMvc` 中的 `@ResponseBody` 。

表明被该注解标记的类可以作为 **响应体**。

### 属性

| 属性                      | 说明                  | 默认值    | 示例                       |
|-------------------------|---------------------|--------|--------------------------|
| `msgId`                 | 消息类型                | --     | `{0x8103}`、`{0x8001}`    |
| `maxPackageSize`        | 单个数据包的最大大小(转义之前)    | `1024` | `1024` 、` 2048`          |
| `desc`                  | 描述信息                | `""`   | `"Some description ..."` |
| `reversedBit15InHeader` | 消息体属性中保留的第15个 `bit` | `0`    |                          |

### 示例

```java{1}
@Jt808ResponseBody(msgId = 0x8103, desc = "设置终端参数")
public class RespTerminalSettings {
    @ResponseField(order = 2, dataType = MsgDataType.LIST)
    private List<ParamItem> paramList;
    // ...
}
```

## @ResponseField

### 属性

| 属性                             | 说明                                  | 默认值  | 示例                      |
|--------------------------------|-------------------------------------|------|-------------------------|
| `order`                        | 赋值顺序，值越小优先级越高(不要求连续，只比较大小)          | --   | `0`、`1`、`2`             |
| `dataType`                     | 数据类型                                | --   | `WORD`、`BYTE`           |
| `conditionalOn`                | 支持 `SpEL` , 当且仅当计算结果为 `true` 时才会序列化 | `""` | `"#this.result == 0"`   |
| `customerFieldSerializerClass` | 自定义反序列化器                            | --   | `"XxxSerializer.class"` |

### SpEL

| 属性        | 说明                                 |                               |
|-----------|------------------------------------|-------------------------------|
| `this`    | 正在迭代的当前对象                          | 永不为空                          |
| `request` | 本次请求对应的 `Jt808Request` 实例          | 主动下发消息时为空(因为此时没有 Request 的概念) |
| `header`  | 本次请求对应的 `Jt808Request#header()` 实例 | 主动下发消息时为空(因为此时没有 Request 的概念) |
| `session` | `Jt808Session` 实例                  | 手动编码消息时可能为空                   |

::: tip

`SpEL` 绑定的 `RootObject` 就是当前正在迭代的对象。

所以 `#this.someField` 等价于 `someField`、`#this.someMethod()` 等价于 `someMethod()` (`#this` 可以省略)。

:::

### 示例

```java{7,11,15}
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8100)
public class TerminalRegisterReplyRespMsg {

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
