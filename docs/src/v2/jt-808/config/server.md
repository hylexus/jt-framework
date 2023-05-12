---
icon: object
---

# server

::: info 提示

该章节介绍的是 `jt808.server.xxx` 相关的配置。

:::

## 配置项总览

```yaml
jt808:
  server:
    port: 6808
    boss-thread-count: 0
    worker-thread-count: 0
    idle-state-handler:
      enabled: true
      reader-idle-time: 20m
      writer-idle-time: 0s # disabled
      all-idle-time: 0s # disabled
```

## port

- 类型：`int`
- 默认值：`6808`

`Netty` 服务器的TCP端口。

## boss-thread-count

- 类型：`int`
- 默认值：`0`

默认值 `0` 表示交由 `Netty` 处理。

```java
this.bossGroup=new NioEventLoopGroup(bossThreadCount);
```

## worker-thread-count

- 类型：`int`
- 默认值：`0`

默认值 `0` 表示交由 `Netty` 处理。

```java
this.workerGroup=new NioEventLoopGroup(workThreadCount);
```

## idle-state-handler

该配置项对应 `io.netty.handler.timeout.IdleStateHandler` 的 三个属性：`readerIdleTime`、 `writerIdleTime` 、`allIdelTime`。

如果你的项目不需要 `IdleStateHandler` 的话，将 `jt808.server.idle-state-handler.enabled` 配置为 `false` 即可。

::: danger

- 在 2.0.3(不包括) 以下的版本中这三个配置项的默认值都是 `20m`
- 同时其实现类 `Jt808TerminalHeatBeatHandler` 有 `Bug`，详情见: [issues#66](https://github.com/hylexus/jt-framework/issues/66)

:::

**2.0.3** 之后版本默认配置如下：

```yaml
jt808:
  server:
    idle-state-handler:
      enabled: true
      reader-idle-time: 20m # 20m内没有发送数据的客户端将被断开
      writer-idle-time: 0s # disabled
      all-idle-time: 0s # disabled
```
