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

请移步 [配置文件文档](../config/)。

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

```java
@Override
public void configureMsgConverterMapping(MsgConverterMapping mapping) {
    super.configureMsgConverterMapping(mapping);
    mapping.registerConverter(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new MyLocationMsgConverter());
}
```

## MsgHandler

```java
@Override
public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
    super.configureMsgHandlerMapping(mapping);
    mapping.registerHandler(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationInfoUploadMsgHandler());
}
```

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
