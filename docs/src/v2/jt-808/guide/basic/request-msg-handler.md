---
icon: wrap
---

# 请求消息处理器

## 介绍

::: tip

**接收/处理** 请求没有强制指定固定类型的处理器，由谁来处理消息取决于 `Jt808HandlerMapping` 配置。

:::

内置了两个 `Jt808HandlerMapping`:

<p class="demo">
    <img :src="$withBase('/img/v2/design/jt808-handler-mapping.png')">
</p>

- `SimpleJt808RequestHandlerHandlerMapping` 能将消息路由到 `SimpleJt808RequestHandler` 类型的 **类级别** 处理器。
- `Jt808RequestHandlerMappingHandlerMapping` 能将消息路由到 `@Jt808RequestHandlerMapping` 注解修饰的 **方法级别** 处理器。

## @Jt808RequestHandlerMapping

### 介绍

被 `@Jt808RequestHandlerMapping` 标记的这种处理器也是从 `Spring` 借鉴(抄袭)的。和 `Spring` 的 `@Controller`、`@RestController` 用法类似。

作用就是标记一下当前类是一个消息处理器类。被该注解标记的类中应该存在若干个能处理请求的方法。

### 示例

@[code java{3-4}](@example-src/808/v2/basic/request-processing/Jt808RequestHandlerAnnotation.java)

## SimpleJt808RequestHandler

### 介绍

最直观的处理方式就是类似于 `Spring` 的 `WebFlux` 中的 `org.springframework.web.reactive.function.server.HandlerFunction`。

但是 `HandlerFunction` 需要借助 `RouterFunction` 来将请求和 `HandlerFunction` 联系起来。

本项目借鉴(抄袭) `WebFlux`，提供了一个  `SimpleJt808RequestHandler`(为了编码简单，直接省去了 `RouterFunction`) :

::: code-tabs#HandlerFunction(Spring)

@tab HandlerFunction(Spring)

```java
// 这里是 Spring WebFlux 的 HandlerFunction 定义
package org.springframework.web.reactive.function.server;

public interface HandlerFunction<T extends ServerResponse> {

    Mono<T> handle(ServerRequest request);

}
```

@tab SimpleJt808RequestHandler:active

```java
public interface SimpleJt808RequestHandler<T> extends MultipleVersionSupport {

    /**
     * @return 该处理器可以处理什么类型的消息
     */
    Set<MsgType> getSupportedMsgTypes();

    /**
     * @return 该处理器可以处理的协议类型(默认为ALL)
     */
    @Override
    default Set<Jt808ProtocolVersion> getSupportedVersions() {
        return MultipleVersionSupport.super.getSupportedVersions();
    }

    /**
     * 处理消息
     *
     * @return {@link Jt808Response} 或 可以转换为 {@link Jt808Response} 的类型
     */
    T handleMsg(Jt808ServerExchange exchange);

}
```

:::

### 示例

::: tip 使用方式

直接将实现了 `SimpleJt808RequestHandler` 接口的处理器类加入到 `Spring` 容器中就可以。

:::

@[code java{2-3}](@example-src/808/v2/basic/request-processing/SimpleJt808RequestHandler.java)

