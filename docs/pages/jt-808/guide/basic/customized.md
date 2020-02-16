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
- 如果你覆盖了/未启用内置的 `AuthMsgHandler`，那么你也 `不用` 提供 `AuthCodeValidator`。因为此时的鉴权逻辑已经完全交由你自定的 `AuthMsgHandler` 来处理了。
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
- 当然，这种实现接口并手动注册的方式显得非常繁琐，你完全可以 [参考这里](../annotation-based-dev/req-msg-mapping.md) 使用 `基于注解` 的方式来实现 `RequestMsgBodyConverter` 的功能。
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
基于注解来实现 `RequestMsgBodyConverter` 的功能 [请移步这里](../annotation-based-dev/req-msg-mapping.md)
:::


## MsgHandler

`MsgHandler` 负责处理经过 `RequestMsgBodyConverter` 转换之后的 `请求体消息实体类`。

对于请求消息的处理完全是由 `MsgHandler` 接口来实现的。

```java
public interface MsgHandler<T extends RequestMsgBody> extends OrderedComponent {

    default Set<MsgType> getSupportedMsgTypes() {
        return Sets.newHashSet();
    }

    void handleMsg(RequestMsgMetadata metadata, T body, Session session) throws IOException, InterruptedException;

}
```

::: danger MsgHandler
- 自定义的消息处理器 `必须` 实现 `MsgHandler` 这个泛型接口
- 当然，这种实现接口并手动注册的方式显得非常繁琐，你完全可以 [参考这里](../annotation-based-dev/msg-handler-register.md#@jt808requestmsghandlermapping) 使用 `基于注解` 的方式来实现 `MsgHandler` 的功能。
:::

消息处理完成后对客户端的响应也是一个 `byte[]` ，可以通过 `handleMsg()` 方法的 `Session` 参数中拿到 `Netty` 的 `Channel`，然后通过 `Channel` 发送给客户端。

- 所以一般的消息处理步骤应该像下面这样：

```java
@Slf4j
public class SampleMsgHandler implements MsgHandler<AuthRequestMsgBody> {

    byte SUCCESS = 0;
    byte AUTH_CODE_ERROR = 2;

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Collections.singleton(BuiltinJt808MsgType.CLIENT_AUTH);
    }

    @Override
    public void handleMsg(RequestMsgMetadata metadata, AuthRequestMsgBody body, Session session) throws IOException, InterruptedException {
        final String authCode = body.getAuthCode();

        // 鉴权逻辑
        final byte result = isValidAuthCode(authCode, session.getTerminalId())
                ? SUCCESS
                : AUTH_CODE_ERROR;

        // 组装响应消息的字节数组(别忘了转义)
        final byte[] respMsgBody = this.encodeMsgBody(result, metadata, session);

        // 发送给客户端
        this.send2Client(session.getChannel(), respMsgBody);
    }

    private byte[] encodeMsgBody(byte result, RequestMsgMetadata metadata, Session session) {
        // ...
        // 按文档格式组装字节数组
        return new byte[0];
    }

    private boolean isValidAuthCode(String authCode, String terminalId) {
        // ...
        // 具体鉴权逻辑
        return true;
    }

    protected void send2Client(Channel channel, byte[] bytes) throws InterruptedException {
        final ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
        if (!future.isSuccess()) {
            log.error("ERROR : 'send data to client:'", future.cause());
        }
    }
}
```

- 又或者像这样
    - 继承 `AbstractMsgHandler`
    - 这里的 `RespMsgBody.toBytes()` 不用转义

```java
@Slf4j
@BuiltinComponent
public class AuthMsgHandler extends AbstractMsgHandler<AuthRequestMsgBody> {

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return newHashSet(BuiltinJt808MsgType.CLIENT_AUTH);
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, AuthRequestMsgBody body, Session session) {
        log.debug("receive AuthMsg : {}", body);
        boolean valid = authCodeValidator.validateAuthCode(session, metadata, body);
        if (valid) {
            return of(commonSuccessReply(metadata, BuiltinJt808MsgType.CLIENT_AUTH));
        }
        return of(CommonReplyMsgBody.of(AUTH_CODE_ERROR, metadata.getHeader().getFlowId(), BuiltinJt808MsgType.CLIENT_AUTH));
    }
}
```

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
基于注解来实现MsgHandler的功能 [请移步这里](../annotation-based-dev/msg-handler-register.md#@Jt808RequestMsgHandlerMapping)
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
