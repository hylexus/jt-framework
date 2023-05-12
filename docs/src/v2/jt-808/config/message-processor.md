---
icon: process
---

# message-processor

::: info 提示

该章节介绍的是 `jt808.msg-processor.executor-group.xxx` 消息处理线程池相关的配置。

参见 `io.netty.util.concurrent.DefaultEventExecutorGroup` 。

:::

## 配置项总览

```yaml
jt808:
  msg-processor:
    executor-group:
      pool-name: 808-msg-processor
      thread-count: 128
      max-pending-tasks: 128
```

## ~~msg-processor.thread-pool~~

::: danger 提示

`msg-processor.thread-pool.xxx` 系列配置在 `2.0.2` 中已经废弃(存在跨线程资源回收问题)。

使用 `msg-processor.executor-group` 代替。

:::

## msg-processor.executor-group

### thread-count

- 类型：`int`
- 默认值：
    - **v2.2.1** 之前: `Runtime.getRuntime().availableProcessors() * 2`
    - **v2.2.1** 之后: **128**

### max-pending-tasks

- 类型：`int`
- 默认值：`128`

### pool-name

线程池的线程名前缀。

- 类型：`String`
- 默认值：`808-msg-processer`
- 默认值：
    - **v2.2.1** 之前: `808-msg-processer`
    - **v2.2.1** 之后: `808-msg-processor`
