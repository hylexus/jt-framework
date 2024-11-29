---
icon: process
---

# message-handler

::: info 提示

该章节介绍的是 `jt808.msg-handler.xxx` 消息处理线程池相关的配置。

参见 `java.util.concurrent.ThreadPoolExecutor` 。

:::

## 配置项总览

```yaml
jt808:
  msg-handler:
    enabled: true
    core-pool-size: 128
    max-pool-size: 256
    keep-alive: 1m
    max-pending-tasks: 256
    daemon: true
    thread-name-prefix: 808-handler
```

## core-pool-size

参考 `java.util.concurrent.ThreadPoolExecutor.corePoolSize`。

- 类型：`int`
- 默认值：128

## max-pool-size

参考 `java.util.concurrent.ThreadPoolExecutor.maximumPoolSize`。

- 类型：`int`
- 默认值：256

## keep-alive

参考 `java.util.concurrent.ThreadPoolExecutor.keepAliveTime`。

- 类型：`java.time.Duration`
- 默认值：`1m`

## max-pending-tasks

参考`java.util.concurrent.ThreadPoolExecutor.workQueue`。

- 类型：`int`
- 默认值：`256`

## daemon

- 类型：`boolean`
- 默认值：`true`

## pool-name

线程池的线程名前缀。

- 类型：`String`
- 默认值：`808-handler`
