---
icon: lock
---

# features(v2.1.1)

::: info 提示

该章节介绍的是 `jt808.features.xxx` 相关的配置。

:::

## 配置项总览

```yaml
jt808:
  features:
    request-filter:
      enabled: false
    program-param-printer:
      enabled: false
      pretty: true
```

## request-filter

**v2.1.1** 中引入了 `Jt808RequestFilter`，但是默认是没有启用的。

如果你要使用 `Jt808RequestFilter`，请打开 `jt808.features.request-filter.enabled = true` 这个配置。

## program-param-printer

以 `JSON` 格式打印 `jt-808.xxx` 开头的所有配置项在运行时读取到的最终值。

::: warning

`jt808.features.program-param-printer.enabled` 默认是 `false`。

如果你要开启这个配置，请确保 直接或间接 引入了 `com.fasterxml.jackson.core:jackson-databind` 和 `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` 依赖。

:::

`jt808.features.program-param-printer.pretty` 用来控制打印的 `JSON` 是否是格式化过的。