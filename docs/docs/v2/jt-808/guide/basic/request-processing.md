# 处理请求消息

在本项目中，所有请求的处理都是一个风格 ：

- 接收 `Jt808Request` (或由 `Jt808Request` 转换出来的其他类型)
- 处理业务逻辑
- 返回 `Jt808Response` (或可以转换为 `Jt808Response` 的其他类型)

## SimpleJt808RequestHandler

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


::: tip 使用方式

直接将实现了 `SimpleJt808RequestHandler` 接口的处理器类加入到 `Spring` 容器中就可以。

:::

