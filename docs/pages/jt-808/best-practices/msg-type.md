# MsgType

`MsgType` 接口是对808协议文档中消息类型的封装。
每个厂家/每套精简版的协议中对消息Id的定义都不尽相同，所以定义了该接口。

`MsgType` 接口的声明如下：

```java
public interface MsgType {
    int getMsgId();

    String getDesc();

    default Optional<MsgType> parseFromInt(int msgId) {
        throw new UnsupportedOperationException("this method should be override in subclass");
    }

    String toString();
}
```

对应该接口的实现，建议使用枚举。

```java
@Getter
public enum Jt808MsgType implements MsgType {
    CLIENT_AUTH(0x0102, "终端鉴权"),
    CLIENT_LOCATION_INFO_UPLOAD(0x0200, "位置上报"),
    REQ_QUERY_LOCK_PARAM_REPLY(0x0104, "查询锁参数应答"),
    ;

    private int msgId;
    private String desc;

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