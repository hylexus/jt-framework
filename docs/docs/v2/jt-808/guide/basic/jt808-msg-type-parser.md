# Jt808MsgTypeParser

## MsgType

**消息ID** 用 `MsgType` 来表示。

::: danger

每个项目都应该提供一个自己的 `MsgType` 实现，参考下面的 `MyMsgType` 定义：

:::

```java
public interface MsgType {

    /**
     * @return 消息ID(消息头中的前两个字节)
     */
    int getMsgId();

    /**
     * @param msgId 消息ID
     * @return 转换之后的 {@link MsgType} 实例
     */
    Optional<MsgType> parseFromInt(int msgId);

    String getDesc();

    String toString();
}
```

```java
public enum MyMsgType implements MsgType {
    CLIENT_REGISTER(0x0100, "终端注册"),
    CLIENT_AUTH(0x0102, "终端鉴权"),
    // ...
    ;

    private final int msgId;
    private final String desc;

    MyMsgType(int msgId, String desc) {
        this.msgId = msgId;
        this.desc = desc;
    }

    private final static Map<Integer, MsgType> mappings = new HashMap<>(values().length);

    static {
        for (MyMsgType value : values()) {
            mappings.put(value.msgId, value);
        }
    }

    @Override
    public Optional<MsgType> parseFromInt(int msgId) {
        return Optional.ofNullable(mappings.get(msgId));
    }

    @Override
    public int getMsgId() {
        return msgId;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
```

## Jt808MsgTypeParser

`Jt808MsgTypeParser` 负责解析 `MsgType`。

::: danger

每个项目都应该提供一个自己的 `Jt808MsgTypeParser` 实现并加入到 `Spring` 容器中。参考下面的 `MyJt808Config` 定义：

:::

```java
public interface Jt808MsgTypeParser {

    Optional<MsgType> parseMsgType(int msgId);
}
```

每个项目都必须提供一个自定义的 `Jt808MsgTypeParser`：

```java

@Configuration
public class MyJt808Config {

    @Bean
    public Jt808MsgTypeParser jt808MsgTypeParser() {
        // 优先使用自定义类型解析
        return msgId -> MyMsgType.CLIENT_AUTH.parseFromInt(msgId)
                // 使用内置类型解析
                .or(() -> BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgId));
    }
}
```