# 响应消息处理器

## 说明

::: tip

对响应给客户端的消息类型没有强制指定固定类型，任何类型的消息处理器返回的结果都被包装成了 `Jt808HandlerResult`。

而 `Jt808HandlerResult` 是由 `Jt808HandlerResultHandler` 处理的。

所以，支持哪些类型的响应消息取决于 `Jt808HandlerResultHandler` 的配置。

:::

内置了两个 `Jt808HandlerResultHandler`:

<p class="demo">
    <img :src="$withBase('/img/v2/design/jt808-handler-result-handler.png')">
</p>

- `Jt808ResponseHandlerResultHandler` 能处理 `Jt808Response` 类型的响应数据。
- `Jt808ResponseBodyHandlerResultHandler` 能将处理被 `@Jt808ResponseBody` 注解修饰的返回类型。

::: tip

本小节主要介绍 `Jt808ResponseHandlerResultHandler` 类型的 **类级别** 处理器，毕竟当注解不方便处理请求时，这种类型的 **HandlerResultHandler** 是内置的 **HandlerResultHandler** 中唯一的选择。

[//]: # (基于注解的请求消息处理，请移步 todo)

:::

## Jt808Response

### 说明

### 示例

<CodeGroup>

  <CodeGroupItem title="示例1" active>

@[code](@example-src/808/v2/basic/response-processing/SimpleLocationInfoUploadHandlerSimple.java)

  </CodeGroupItem>

  <CodeGroupItem title="示例2">

@[code](@example-src/808/v2/basic/response-processing/TerminalRegisterMsgHandlerV2013.java)

  </CodeGroupItem>


</CodeGroup>

## @Jt808ResponseBody

### 示例

### 说明