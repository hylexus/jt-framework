---
icon: plugin
---

# 请求生命周期监听器

## 介绍

一个终端请求的处理，包含下面几个比较关键的步骤：

- `decode`: 将 `ByteBuf` 初步解码为 `Jt808Request`
    - 此时消息整体结构已经解码完成
    - `body` (消息体)部分依旧是 `ByteBuf`
- `dispatch1`: 将上一步初步解码的 `Jt808Request` 对象 **分发** 出去
    - 如果是分包请求
        - 在 `AbstractJt808RequestMsgQueueListener` 里暂存分包
        - 直到所有子包都到达之后自动合并请求，再次 `dispatch` 合并后的请求
    - 否则，进行下一步
- `dispatch2`: 将请求(包括合并后的分包请求)分发给处理器链 `Jt808DispatcherHandler`
    - 调用处理器处理请求
    - 处理 处理器返回的结果
    - 回复终端

对应上面描述的处理流程，提供了 `Jt808RequestLifecycleListener` 以便在消息处理流程的关键步骤:

- 加入自己的逻辑
- 拦截请求的处理流程

::: details 点击展开 Jt808RequestLifecycleListener 接口声明

@[code{74-}](@example-src/808/v2/customization/Jt808RequestLifecycleListener.java)

:::

## 使用示例

这里是一个使用 `Prometheus` 统计请求次数的示例：

@[code{13-}](@example-src/808/v2/customization/PrometheusMetricsExporter.java)
