# 消息处理器

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
- 当然，这种实现接口并手动注册的方式显得非常繁琐，你完全可以 [参考这里](src/v1/jt-808/guide/annotation-based-dev/msg-handler-register.md#@jt808requestmsghandlermapping) 使用 `基于注解` 的方式来实现 `MsgHandler` 的功能。
:::

消息处理完成后对客户端的响应也是一个 `byte[]` ，可以通过 `handleMsg()` 方法的 `Session` 参数中拿到 `Netty` 的 `Channel`，然后通过 `Channel` 发送给客户端。

## 手动实现并注册

### 实现

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

### 注册自定义MsgHandler

```java
public class Jt808Config extends Jt808ServerConfigurationSupport { 
    @Override
    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
        super.configureMsgHandlerMapping(mapping);
        // 如果你在这里注册了自定义的鉴权消息处理器，那么AuthCodeValidator也无需提供了
        // 此处也可以从Spring容器中获取bean来注入，不一定要手动new一个Handler注册
        mapping.registerConverter(Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD, new LocationInfoUploadMsgHandler());
    }
}
```

## 基于注解实现

::: tip 传送门
基于注解来实现MsgHandler的功能 [请移步这里](src/v1/jt-808/guide/annotation-based-dev/msg-handler-register.md#@Jt808RequestMsgHandlerMapping)
:::

---

::: tip 传送门
本小节示例可以在 [samples/jt-808-server-sample-customized](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-customized) 找到相关代码。
:::