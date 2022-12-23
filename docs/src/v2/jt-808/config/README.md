---
sidebarDepth: 3
icon: config
---

# 808服务配置

本小节会介绍808服务的配置选项。

默认的配置可以在
[default-jt808-server-config.yml](https://github.com/hylexus/jt-framework/tree/master/jt-808-server-spring-boot-stater/src/main/resources/META-INF/default-jt808-server-config.yml)
中查看。 并且已经将默认的配置加入到了 `Spring` 的 `PropertySources` 中，并将其置于最后，名称为 `default-jt808-server-config` 。

<p class="">
    <img :src="$withBase('/img/default-config-property-source.png')" alt="default-config-property-source">
</p> 

## 配置项总览

```yaml
jt808:
  built-components:
  # ...
  protocol:
  # ...
  server:
  # ...
  msg-processor:
  # ...
  request-sub-package-storage:
  # ...
  response-sub-package-storage:
  # ...
```

## 默认配置

默认的配置可以在
[default-jt808-server-config.yml](https://github.com/hylexus/jt-framework/tree/master/jt-808-server-spring-boot-stater/src/main/resources/META-INF/default-jt808-server-config.yml)
中查看。 并且已经将默认的配置加入到了 `Spring` 的 `PropertySources` 中，并将其置于最后，名称为 `default-jt808-server-config` 。

## protocol

### max-frame-length

对应 `io.netty.handler.codec.DelimiterBasedFrameDecoder` 的 `maxFrameLength` 属性。默认值：`1024`。

## server

### port

- 类型：`int`
- 默认值：`6808`

`Netty` 服务器的TCP端口。

### boss-thread-count

- 类型：`int`
- 默认值：`0`

默认值 `0` 表示交由 `Netty` 处理。

```java
this.bossGroup=new NioEventLoopGroup(bossThreadCount);
```

### worker-thread-count

- 类型：`int`
- 默认值：`0`

默认值 `0` 表示交由 `Netty` 处理。

```java
this.workerGroup=new NioEventLoopGroup(workThreadCount);
```

### idle-state-handler

该配置项对应 `io.netty.handler.timeout.IdleStateHandler` 的 三个属性：`readerIdleTime`、 `writerIdleTime` 、`allIdelTime`。 默认值都是 `20m`;

如果你的项目不需要 `IdleStateHandler` 的话，将 `jt808.server.idle-state-handler.enabled` 配置为 `false` 即可。

## ~~msg-processor.thread-pool~~

::: danger 提示

`msg-processor.thread-pool.xxx` 系列配置在 `2.0.2` 中已经废弃(存在跨线程资源回收问题)。

使用 `msg-processor.executor-group` 代替。

:::

## msg-processor.executor-group

::: tip 此处为 `消息处理线程池` 相关的配置。

参见 `io.netty.util.concurrent.DefaultEventExecutorGroup` 。

:::

### thread-count

- 类型：`int`
- 默认值：`Runtime.getRuntime().availableProcessors() * 2`

### max-pending-tasks

- 类型：`int`
- 默认值：`128`

### pool-name

- 类型：`String`
- 默认值：`808-msg-processer`

线程池名称。

## request-sub-package-storage

请求分包消息暂存器相关配置。

```yaml
  request-sub-package-storage:
    type: caffeine # caffeine || none
    ## 当且仅当 jt808.request-sub-package-storage.type = caffeine 时生效
    caffeine:
      # 最多缓存多少条消息
      maximum-size: 1024
      # 最大缓存时间
      ttl: 45s
```

## response-sub-package-storage

响应分包消息暂存器相关配置。

```yaml
    response-sub-package-storage:
    type: caffeine # caffeine || redis || none
    ## 当且仅当 jt808.response-sub-package-storage.type = caffeine 时生效
    caffeine:
      # 最多缓存多少条消息
      maximum-size: 1024
      # 最大缓存时间
      ttl: 45s
    ## 当且仅当 jt808.response-sub-package-storage.type = redis 时生效
    redis:
      # 最大缓存时间
      ttl: 1m
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

[//]: # (todo print-component-statistics.png')" alt="print-component-statistics)

### request-handlers.enabled

- 类型：`boolean`
- 默认值：`true`

`jt808.built-components.request-handlers.enabled` 表示是否启用内置的一些消息处理器。

内置消息处理器都在 `io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin` 包下。

## logging

```yaml
logging:
  level.root: info
  level.io.github.hylexus: info
  # 对应 io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder 的日志
  level.jt-808.request.decoder: info
  # 对应 io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder 的日志
  level.jt-808.response.encoder: info
```
