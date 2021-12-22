# 请求消息处理器

## 说明

::: tip

**接收/处理** 请求没有强制指定固定类型的处理器，由谁来处理消息取决于 `Jt808HandlerMapping` 配置。

:::

内置了两个 `Jt808HandlerMapping`:

<p class="demo">
    <img :src="$withBase('/img/v2/design/jt808-handler-mapping.png')">
</p>

- `SimpleJt808RequestHandlerHandlerMapping` 能将消息路由到 `SimpleJt808RequestHandler` 类型的 **类级别** 处理器。
- `Jt808RequestHandlerMappingHandlerMapping` 能将消息路由到 `@Jt808RequestHandlerMapping` 注解修饰的 **方法级别** 处理器。

## SimpleJt808RequestHandler

### 介绍

最直观的处理方式就是类似于 `Spring` 的 `WebFlux` 中的 `org.springframework.web.reactive.function.server.HandlerFunction`。

但是 `HandlerFunction` 需要借助 `RouterFunction` 来将请求和 `HandlerFunction` 联系起来。

本项目借鉴(抄袭) `WebFlux`，提供了一个  `SimpleJt808RequestHandler`(为了编码简单，直接省去了 `RouterFunction`) :

<CodeGroup>

  <CodeGroupItem title="HandlerFunction(Spring)">

```java
// 这里是 Spring WebFlux 的 HandlerFunction 定义
package org.springframework.web.reactive.function.server;

public interface HandlerFunction<T extends ServerResponse> {

    Mono<T> handle(ServerRequest request);

}
```

  </CodeGroupItem>

  <CodeGroupItem title="SimpleJt808RequestHandler" active>

@[code](@example-src/808/v2/basic/SimpleJt808RequestHandler.java)

  </CodeGroupItem>

</CodeGroup>

### 示例

::: tip 使用方式

直接将实现了 `SimpleJt808RequestHandler` 接口的处理器类加入到 `Spring` 容器中就可以。

:::

<CodeGroup>

  <CodeGroupItem title="示例1">

@[code](@example-src/808/v2/basic/request-processing/TerminalRegisterMsgHandlerV2011.java)

  </CodeGroupItem>

  <CodeGroupItem title="示例2">

@[code](@example-src/808/v2/basic/request-processing/TerminalRegisterMsgHandlerV2013.java)

  </CodeGroupItem>

  <CodeGroupItem title="示例3" active>

@[code](@example-src/808/v2/basic/request-processing/TerminalRegisterMsgHandlerV2019.java)

  </CodeGroupItem>

</CodeGroup>

## @Jt808RequestHandlerMapping

### 介绍

这种处理器也是从 `Spring` 借鉴(抄袭)的。和 `Spring` 的 `@Controller`、`@RestController` 用法类似。

作用就是标记一下当前类是一个消息处理器类。

### 示例

```java
@Component
@Jt808RequestHandler
public class BuiltinTerminalAuthenticationMsgHandler {

    // 7E0102000C013912344321007B61646D696E2D3132333435364D7E
    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = {VERSION_2013, VERSION_2011})
    public BuiltinServerCommonReplyMsg authMsgV2013(Jt808Request request, BuiltinMsg0102V2013 body) {
        log.info("V2013--auth : {}", body);
        return new BuiltinServerCommonReplyMsg()
                .setClientFlowId(request.flowId())
                .setClientMsgId(request.msgType().getMsgId())
                .setResult(0);
    }

    @Jt808RequestHandlerMapping(msgType = 0x0102, versions = VERSION_2019)
    public BuiltinServerCommonReplyMsg authMsgV2019(Jt808Request request, BuiltinMsg0102V2019 body) {
        log.info("V2019--auth : {}", body);
        return new BuiltinServerCommonReplyMsg()
                .setClientFlowId(request.flowId())
                .setClientMsgId(request.msgType().getMsgId())
                .setResult(0);
    }
}
```

