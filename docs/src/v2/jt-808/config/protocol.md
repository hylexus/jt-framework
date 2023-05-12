---
icon: proposal
---

# protocol

::: info 提示

该章节介绍的是 `jt808.protocol.xxx` 相关的配置。

:::

## 配置项总览

```yaml
jt808:
  protocol:
    max-frame-length: 1024
```

## max-frame-length

对应 `io.netty.handler.codec.DelimiterBasedFrameDecoder` 的 `maxFrameLength` 属性。默认值：`1024`。

