---
icon: wrap
---

# 请求参数解析器

## 介绍

这部分也是借鉴(抄袭) `Spring` 的。

上一小节介绍了怎么接收请求，但是接收到的都是 `Jt808Request` 类型。

就类似于 `JavaWeb` 开发中的 `Servlet` 一样给你一个 `HttpServletRequest` : 后续所有解析操作都要手动进行，比较繁琐。

所以本项目也提供了几个内置的 `Jt808HandlerMethodArgumentResolver` 来将原始的 `Jt808Request`
转换成可读性较强的自定义类型，这些转换功能都是依赖于参数解析器 `Jt808HandlerMethodArgumentResolver`：

```java
public interface Jt808HandlerMethodArgumentResolver {

    boolean supportsParameter(MethodParameter methodParameter);

    Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws Jt808ArgumentResolveException;

}
```

## 支持的参数类型

内置的几种 **参数解析器** 如下：

<p class="demo">
    <img :src="$withBase('/img/v2/design/arg-resolver.png')" />
</p>

在默认配置的情况下，你可以按需在 **请求处理器方法参数** 中使用如下类型中的若干个：

- `Jt808Request`
- `Jt808RequestHeader`
- `Jt808Response`
- `Jt808Session`
- `Jt808ServerExchange`
- 被`@Jt808RequestBody`标记的自定义类型
- `Jt808RequestEntity`

## Jt808Request

在处理器方法中注入 `Jt808Request` 类型参数：

```java{6}
@Component
@Jt808RequestHandler
public class AnnotationHandler01 {

    @Jt808RequestHandlerMapping(msgType = 0x0001, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    public void processMsg0001(Jt808Request request) {
        // ...
    }
}
```

## Jt808Response

在处理器方法中注入 `Jt808Response` 类型参数：

```java{6}
@Slf4j
@Component
@Jt808RequestHandler
public class AnnotationHandler01 {
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    public Jt808Response process(Jt808Request request, Jt808Response response) {
        // ... process(request)
        return response.msgId(BuiltinJt808MsgType.SERVER_COMMON_REPLY.getMsgId())
                .writeWord(request.flowId())
                .writeByte(0)
                .writeString("AuthCodeXxx");
    }
}
```

## Jt808Session

在处理器方法中注入 `Jt808Session` 类型参数：

```java{6}
@Slf4j
@Component
@Jt808RequestHandler
public class AnnotationHandler01 {
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    public Jt808Response process(Jt808Session session, Jt808Response response, ...) {
        // ... process(request)
        return ...;
    }
}
```

## Jt808ServerExchange

在处理器方法中注入 `Jt808ServerExchange` 类型参数：

```java{5}
@Component
@Jt808RequestHandler
public class AnnotationHandler01 {
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    public Jt808Response process(Jt808ServerExchange exchange) {
        // ... process(exchange.request())
        return exchange.response().msgId(BuiltinJt808MsgType.SERVER_COMMON_REPLY.getMsgId())
                .writeWord(request.flowId())
                .writeByte(0)
                .writeString("AuthCodeXxx");
    }
}
```

## Jt808RequestHeader

在处理器方法中注入 `Jt808ServerExchange` 类型参数：

```java{5}
@Component
@Jt808RequestHandler
public class AnnotationHandler01 {
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    public Jt808Response process(Jt808RequestHeader header, Jt808Response response, ...) {
        // ... process(exchange.request())
        return ...;
    }
}
```

## @Jt808RequestBody

下面的 `DebugTerminalRegisterMsgV2013` 被 `@Jt808RequestBody` 注解标记了，表示给类用来接收请求体。

```java{5}
@Component
@Jt808RequestHandler
public class AnnotationHandler01 {
    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    public Object processRegisterMsgV2013(DebugTerminalRegisterMsgV2013 authMsgV2013, Jt808Request request, Jt808Session session) {
        log.info("{}", authMsgV2013);
        return new TerminalRegisterReplyRespMsg()
                .setFlowId(request.flowId())
                .setResult((byte) 0)
                .setAuthCode("AuthCode2013DebugDemo");
    }

    @Data
    @Jt808RequestBody
    public static class DebugTerminalRegisterMsgV2013 {
        // 1. [0-2) WORD 省域ID
        @RequestField(order = 1, dataType = WORD)
        private int provinceId;
        // ...
    }
}
```

## Jt808RequestEntity

该类和 `Spring` 的 `org.springframework.http.RequestEntity` 类似。包含了本次请求的所有信息。

泛型 `body` 部分的处理逻辑和被 `@Jt808RequestBody` 标记的类的处理逻辑相同。该类定义如下：

```java
public class Jt808RequestEntity<T> {
    private final MsgType msgType;
    private final Jt808RequestHeader header;
    private final ByteBuf rawByteBuf;
    private final T body;
    private final byte originalCheckSum;
    private final byte calculatedCheckSum;
    private final Jt808Session session;
    // ...
}
```

示例如下：

```java{6}
@Component
@Jt808RequestHandler
public class BuiltinCommonHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0704)
    public BuiltinServerCommonReplyMsg processMsg0704(Jt808RequestEntity<BuiltinMsg0704V2013> requestEntity) {
        log.info("BuiltinMsg0704 : {}", requestEntity.body());
        return BuiltinServerCommonReplyMsg.success(requestEntity.msgType().getMsgId(), requestEntity.flowId());
    }
}
```
