---
icon: others
---

# others

::: info 提示

该章节介绍的是除前面章节内容之外的其他杂项配置。

:::

## 配置项总览

```yaml
jt808:
  built-components:
    request-handlers:
      enabled: true
    component-statistics:
      enabled: true
logging:
  level:
    root: info
    io.github.hylexus: info
    jt-808.request.decoder: info
    jt-808.response.encoder: info
```

## built-components

### component-statistics.enabled

- 类型：`boolean`
- 默认值：`true`

`jt808.built-components.component-statistics.enabled` 表示是否开启服务启动完成后显示组件统计信息。

这些统计信息可以显示已经注册的 **消息处理器** 和其他 **可配置的** 组件。类似于下图所示：

<p class="">
    <img :src="$withBase('/img/print-component-statistics.png')" alt="print-component-statistics">
</p> 

### request-handlers.enabled

- 类型：`boolean`
- 默认值：`true`

`jt808.built-components.request-handlers.enabled` 表示是否启用内置的一些消息处理器。

内置消息处理器都在 `io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin` 包下。

::: warning

内置的处理器仅仅是示例性的，不要直接使用。

:::

## logging

```yaml
logging:
  level:
    root: info
    io.github.hylexus: info
    # 对应 io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder 的日志
    jt-808.request.decoder: info
    # 对应 io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder 的日志
    jt-808.response.encoder: info
```
