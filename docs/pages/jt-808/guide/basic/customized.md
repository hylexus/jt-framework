# 定制化

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 下找到对应代码。
:::

::: tip 提示
目前几乎所有的自定义的配置都需要继承配置类 `io.github.hylexus.jt808.support.netty.Jt808ServerConfigure` 。
可以通过如下代码片段的方式来自定义组件：

```java
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Jt808Config extends Jt808ServerConfigure {
    // TODO 重写配置方法或覆盖父类的Bean
}

```
:::

## application.yml

请移步 [配置文件文档](../../config/)。

## BytesEncoder

::: tip 小提示
每个硬件厂商实现808协议的时候对转义的处理可能略有不同。

如果内置的转义逻辑不符合要求，可以自己实现 `BytesEncoder` 接口实现转义逻辑。

之后覆盖内置的处理逻辑即可。
:::

```java
@Override
public BytesEncoder supplyBytesEncoder() {
    return new BytesEncoder() {

        private final BytesEncoder bytesEncoder = new BytesEncoder.DefaultBytesEncoder();

        @Override
        public byte[] doEscapeForReceive(byte[] bytes, int start, int end) throws MsgEscapeException {
            return bytesEncoder.doEscapeForReceive(bytes, start, end);
        }

        @Override
        public byte[] doEscapeForSend(byte[] bytes, int start, int end) throws MsgEscapeException {
            return bytesEncoder.doEscapeForSend(bytes, start, end);
        }
    };
}
```

## MsgTypeParser

- 扩展自己的 `MsgType`

```java
@Getter
@ToString
public enum Jt808MsgType implements MsgType {
    CLIENT_AUTH(0x0102, "终端鉴权"),
    CLIENT_LOCATION_INFO_UPLOAD(0x0200, "位置上报"),
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

- 定制 `MsgTypeParser`

::: warning 注意
`MsgTypeParser` 的返回类型为 `java.util.Optional<MsgType>` 。

即便是无法处理的消息也不要返回 `null` 而应该以 `Optional.empty()` 代替。
此时会跳过这种未知类型的消息处理，并会在日志里打印 `warn` 级别的日志。


:::

```java
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
```

## AuthCodeValidator

::: danger 注意
- 该组件 `当且仅当` 你使用了内置的 `AuthMsgHandler` 来处理 `鉴权消息` 时才有效。
- 如果你覆盖/未启用了内置的 `AuthMsgHandler`，那么你也 `不用` 提供 `AuthCodeValidator`。因为此时的鉴权逻辑已经完全交由你自定的 `AuthMsgHandler` 来处理了。
:::

```java
@Override
public AuthCodeValidator supplyAuthCodeValidator() {
    return (session, requestMsgMetadata, authRequestMsgBody) -> {
        final String terminalId = session.getTerminalId();
        final String authCode = authRequestMsgBody.getAuthCode();
        // 从其他服务验证鉴权码是否正确
        boolean success = clientService.isAuthCodeValid(terminalId, authCode);
        log.info("AuthCode validate for terminal : {} with authCode : {}, result: {}", terminalId, authCode, success);
        return success;
    };
}
```

## RequestMsgBodyConverter

::: danger RequestMsgBodyConverter
- 自定义的消息体解析器 `必须` 实现 `RequestMsgBodyConverter` 这个泛型接口
- 当然，这种实现接口并手动注册的方式显得非常繁琐，你完全可以 [参考这里](../annotation-based-dev/receive-msg-mapping.md) 使用 `基于注解` 的方式来实现 `RequestMsgBodyConverter` 的功能。
:::

`RequestMsgBodyConverter` 负责将客户端请求中的 `byte[]` 转换为 `请求消息体实体类` 以方便使用。 

- 以下为示例性的解析位置消息的 `RequestMsgBodyConverter`

```java
public class LocationUploadMsgBodyConverter2 implements RequestMsgBodyConverter<LocationUploadMsgBody> {

    @Override
    public Optional<LocationUploadMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        LocationUploadMsgBody body = new LocationUploadMsgBody();
        body.setWarningFlag(intFromBytes(bytes, 0, 4));
        body.setStatus(intFromBytes(bytes, 4, 4));
        body.setLat(intFromBytes(bytes, 8, 4) * 1.0 / 100_0000);
        body.setLng(intFromBytes(bytes, 12, 4) * 1.0 / 100_0000);
        body.setHeight((short) intFromBytes(bytes, 16, 2));
        body.setSpeed((short) intFromBytes(bytes, 18, 2));
        body.setDirection((short) intFromBytes(bytes, 20, 2));
        body.setTime(BcdOps.bytes2BcdString(bytes, 22, 6));
        return Optional.of(body);
    }

}
```

- 注册自定义 `RequestMsgBodyConverter`

```java
@Override
public void configureMsgConverterMapping(MsgConverterMapping mapping) {
    super.configureMsgConverterMapping(mapping);
    mapping.registerConverter(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationUploadMsgBodyConverter2());
}
```

::: tip 传送门
基于注解来实现 `RequestMsgBodyConverter` 的功能 [请移步这里](../annotation-based-dev/receive-msg-mapping.md)
:::


## MsgHandler

::: danger MsgHandler
- 自定义的消息处理器 `必须` 实现 `MsgHandler` 这个泛型接口
- 当然，这种实现接口并手动注册的方式显得非常繁琐，你完全可以 [参考这里](../annotation-based-dev/msg-handler-register.md#@Jt808RequestMsgMapping) 使用 `基于注解` 的方式来实现 `MsgHandler` 的功能。
:::

`MsgHandler` 负责处理经过 `RequestMsgBodyConverter` 转换之后的 `请求体消息实体类`。

- 以下为示例性的处理位置消息的 `MsgHandler`

```java
@Slf4j
public class LocationInfoUploadMsgHandler extends AbstractMsgHandler<LocationUploadMsgBody> {

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, LocationUploadMsgBody body, Session session) {

        log.info("{}", body);
        return Optional.of(commonSuccessReply(metadata, BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD));
    }
}
```

- 注册自定义 `MsgHandler`

```java
@Override
public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
    super.configureMsgHandlerMapping(mapping);
    // 如果你在这里注册了自定义的鉴权消息处理器，那么AuthCodeValidator也无需提供了
    mapping.registerConverter(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationUploadMsgBodyConverter2());
}
```

::: tip 传送门
基于注解来实现MsgHandler的功能 [请移步这里](../annotation-based-dev/msg-handler-register.md#@Jt808RequestMsgMapping)
:::

## Netty相关配置

```java
@Override
public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
    // ServerBootstrap 等的配置
    super.configureServerBootstrap(serverBootstrap);
}

@Override
public void configureSocketChannel(SocketChannel ch, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
    // SocketChannel、ChannelPipeline 等的配置
    super.configureSocketChannel(ch, jt808ChannelHandlerAdapter);
}
```

::: tip 传送门
本小节的示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 下找到对应代码。
:::
