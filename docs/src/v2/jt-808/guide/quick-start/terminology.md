---
icon: proposal
---

# 基本术语

在 `2.x` 中，所有的核心 `API` 都重写了 (`fluent` 风格)。几个关键接口如下：

## Jt808Request

客户端请求消息中的字节流最终会解析到 `Jt808Request` 里。`Jt808Request` 接口内容如下：

```java
public interface Jt808Request {

    /**
     * @return 消息ID
     */
    MsgType msgType();

    /**
     * @return 请求头
     */
    Jt808RequestHeader header();

    /**
     * @return 原始报文(转义之后)
     */
    ByteBuf rawByteBuf();

    /**
     * @return 消息体(转义之后)
     */
    ByteBuf body();

    /**
     * @return 校验码(原始报文)
     */
    byte originalCheckSum();

    /**
     * @return 校验码(计算后)
     * @see Jt808MsgBytesProcessor#calculateCheckSum(ByteBuf)
     */
    byte calculatedCheckSum();

    // ...
}
```

## Jt808Response

与 `Jt808Request` 相对应，处理完消息之后回复给客户端的数据对应着 `Jt808Response` 接口：

```java
public interface Jt808Response extends Jt808ByteWriter {

    int DEFAULT_MAX_PACKAGE_SIZE = 1024;

    // ...

    /**
     * byte[0,2) -- {@link  MsgDataType#WORD WORD} -- 消息ID
     */
    int msgType();

    Jt808ProtocolVersion version();

    /**
     * {@link Jt808ProtocolVersion#VERSION_2011 VERSION_2011} -- byte[4,10) -- {@link  MsgDataType#BCD BCD[6]} -- 终端手机号
     * <p>
     * {@link Jt808ProtocolVersion#VERSION_2019 VERSION_2019} -- byte[5,15) -- {@link  MsgDataType#BCD BCD[10]} -- 终端手机号
     */
    String terminalId();

    /**
     * {@link Jt808ProtocolVersion#VERSION_2011 VERSION_2011} -- byte[10,11) -- {@link  MsgDataType#WORD WORD} -- 流水号
     * <p>
     * {@link Jt808ProtocolVersion#VERSION_2019 VERSION_2019} -- byte[15,16) -- {@link  MsgDataType#WORD WORD} -- 流水号
     */
    int flowId();

    ByteBuf body();

    // ...
}

```

## Jt808Session

和客户端对应的连接都用一个叫做 `Jt808Session` 类来表示：

```java
public interface Jt808Session extends Jt808FlowIdGenerator {

    // ...

    /**
     * @param byteBuf 待发送给客户端的数据
     */
    void sendMsgToClient(ByteBuf byteBuf) throws JtCommunicationException;

    /**
     * @return 当前流水号，并自增
     * @see #currentFlowId(boolean)
     */
    int currentFlowId();

    default String sessionId() {
        return id();
    }

    Channel channel();

    Jt808Session channel(Channel channel);

    String terminalId();

    /**
     * @return 当前终端的协议版本号
     */
    Jt808ProtocolVersion protocolVersion();

    /**
     * @return 上次通信时间
     */
    long lastCommunicateTimestamp();

    Jt808Session lastCommunicateTimestamp(long lastCommunicateTimestamp);

}
```

## Jt808ServerExchange

在实际处理消息的过程中，将 `Jt808Request`、`Jt808Response` 和 `Jt808Session` 都封装在了一个叫 `Jt808ServerExchange` 的对象里。

和 `Spring` 的 `WebFlux` 中的 `org.springframework.web.server.ServerWebExchange` 有类似的作用。

```java
public interface Jt808ServerExchange {

    Jt808Request request();

    Jt808Response response();

    Jt808Session session();
}
```
