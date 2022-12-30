# 消息处理器注册

::: tip 传送门
- 1. 本小节的示例代码可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-annotation) 下找到对应代码。
- 2. [点击这里了解 MsgHandler 的作用](src/v1/jt-808/guide/customization/msg-handler-config.md)。
:::

## @Jt808RequestMsgHandler

- 该注解类似于 `SpringMVC` 的 `@Controller`。
- `@Jt808RequestMsgHandler` 只能标注于 `类` 上，表示该类中存在 `MsgHandler` 方法。

```java
@Jt808RequestMsgHandler
public class CommonHandler {
    // ...
}
```

## @Jt808RequestMsgHandlerMapping

- 该注解类似于 `SpringMVC` 的 `@RequestMapping`。
- `@Jt808RequestMsgHandlerMapping` 只能标注于 `方法` 上，表示被注解的方法是一个 `MsgHandler` 方法。
- 从逻辑上来理解，被其注解的方法相当于一个实现了 `MsgHandler` 接口的处理器类。

```java
@Slf4j
@Jt808RequestMsgHandler
public class CommonHandler {
    // 处理MsgId为0x0200的消息
    @Jt808RequestMsgHandlerMapping(msgType = 0x0200)
    public RespMsgBody processLocationMsg(
            Jt808Session session, RequestMsgMetadata metadata,
            RequestMsgHeader header, LocationUploadMsgBody msgBody) {

        assert header.getMsgId() == BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId();
        assert session.getTerminalId().equals(header.getTerminalId());
        assert session.getTerminalId().equals(metadata.getHeader().getTerminalId());
        assert metadata.getHeader() == header;

        log.info("处理位置上报消息 terminalId = {}, msgBody = {}", header.getTerminalId(), msgBody);
        return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
    }

}
```

## 参数自动注入

类似于 `SpringMVC` 的处理器参数注入，用 `@Jt808RequestMsgHandlerMapping` 注解的方法支持参数自动注入。

目前支持自动注入的参数类型如下：

- `RequestMsgBody`
- `RequestMsgHeader`
- `RequestMsgMetadata`
- `Jt808Session`

底层由 `HandlerMethodArgumentResolver` 来完成注入。
所有支持的参数解析器都委托给了 `DelegateHandlerMethodArgumentResolvers`。

```java
public class DelegateHandlerMethodArgumentResolvers implements HandlerMethodArgumentResolver {
    // ...
    static void addDefaultHandlerMethodArgumentResolver(DelegateHandlerMethodArgumentResolvers resolvers) {
        resolvers.addResolver(new RequestMsgBodyArgumentResolver());
        resolvers.addResolver(new RequestMsgHeaderArgumentResolver());
        resolvers.addResolver(new RequestMsgMetadataArgumentResolver());
        resolvers.addResolver(new SessionArgumentResolver());
        resolvers.addResolver(new ExceptionArgumentResolver());
    }
    public DelegateHandlerMethodArgumentResolvers() {
        addDefaultHandlerMethodArgumentResolver(this);
    }
    // ...
}
```

## MsgHandler注入到Spring容器

从 `1.0.4-RELEASE` 开始，支持将 `MsgHandler` 交给Spring来管理，以便于在 `MsgHandler` 中使用Spring容器的其他bean。

使用方法也很简单，给你的 `MsgHandler` 加一个 `@Component` 注解即可。

::: tip 传送门
- 1. 本小节的示例代码可以在 [samples/jt-808-server-sample-annotation](https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-annotation) 下找到对应代码。
- 2. [点击这里了解 MsgHandler 的作用](src/v1/jt-808/guide/customization/msg-handler-config.md)。
:::
