# 消息类型

::: tip
- 各个厂商对808协议都是选择性的实现，同时还有自定义的消息类型。
- 所以，并没有内置所有类型的消息，而是定义了一个 `MsgType` 接口以供用户自行扩展。
- 建议实现接口的时候使用枚举
:::

## 扩展MsgType

```java
@Getter
@ToString
public enum Jt808MsgType implements MsgType {
    CLIENT_AUTH(0x0102, "终端鉴权"),
    CLIENT_LOCATION_INFO_UPLOAD(0x0200, "位置上报"),
    ;
    private final int msgId;
    private final String desc;

    Jt808MsgType(int msgId, String desc) {
        this.msgId = msgId;
        this.desc = desc;
    }

    private static final Map<Integer, Jt808MsgType> mapping = new HashMap<>(values().length);

    static {
        for (Jt808MsgType type : values()) {
            mapping.put(type.msgId, type);
        }
    }

    @Override
    public Optional<MsgType> parseFromInt(int msgId) {
        return Optional.ofNullable(mapping.get(msgId));
    }
}
```



## 定制MsgTypeParser

::: warning 注意
`MsgTypeParser` 的返回类型为 `java.util.Optional<MsgType>` 。

即便是无法处理的消息也不要返回 `null` 而应该以 `Optional.empty()` 代替。

此时会跳过这种未知类型的消息处理，并会在日志里打印 `warn` 级别的日志。
:::

```java
@Configuration
public class Jt808Config extends Jt808ServerConfigurationSupport { 
    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return msgType -> {
            // 先使用自定义解析器
            Optional<MsgType> type = Jt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
            return type.isPresent()
                    ? type
                    // 自定义解析器无法解析,使用内置解析器
                    : BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
        };
    }
}
```

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::