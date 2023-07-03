---
headerDepth: 3
---

# 808服务配置(v1)

本小节会介绍808服务的配置选项。

默认的配置可以在 
[default-jt808-server-config.yml](https://github.com/hylexus/jt-framework/tree/master/jt-808-server-spring-boot-stater/src/main/resources/META-INF/default-jt808-server-config.yml) 
中查看。
并且已经将默认的配置加入到了 `Spring` 的 `PropertySources` 中，并将其置于最后，名称为 `default-jt808-server-config` 。

<p class="">
    <img :src="$withBase('/img/default-config-property-source.png')" alt="default-config-property-source">
</p> 

## 配置项

```yaml
jt808:
  print-component-statistics: true
  server:
    # ...
  entity-scan:
    # ...
  handler-scan:
    # ...
  exception-handler-scan:
    # ...
  msg-processor:
    thread-pool:
      # ...
```
## protocol

### version

- 类型：`enum`
- 默认值：`auto_detection`

808协议版本，可选值位于：`io.github.hylexus.jt.config.Jt808ProtocolVersion` 。

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
this.bossGroup = new NioEventLoopGroup(bossThreadCount);
```
### worker-thread-count

- 类型：`int`
- 默认值：`0`

默认值 `0` 表示交由 `Netty` 处理。

```java
this.workerGroup = new NioEventLoopGroup(workThreadCount);
```

### idle-state-handler

该配置项对应 `io.netty.handler.timeout.IdleStateHandler` 的 三个属性：`readerIdleTime`、 `writerIdleTime` 、`allIdelTime`。 默认值都是 `20m`;

如果你的项目不需要 `IdleStateHandler` 的话，将 `jt808.server.idle-state-handler.enabled` 配置为 `false` 即可。

## entity-scan

### enabled

- 类型：`boolean`
- 默认值：`true`

是否启用实体扫描功能。启用后，请求报文可以自动映射到使用 `@Jt808ReqMsgBody` 标记的 `请求消息体实体类` 。

使用基于注解的 `请求体消息实体类` [请参考这里](src/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.md)。

### enable-builtin-entity

- 类型：`boolean`
- 默认值：`true`

是否自动注册内置的 `请求消息体实体类`。
内置的 `请求消息体实体类` 包括：

- `AuthRequestMsgBody` : 鉴权消息 `请求消息体实体类`。
- `EmptyRequestBody` : 一个空的 `请求消息体实体类`。

### base-packages

- 类型：`String`
- 默认值：`""`

`请求消息体实体类` 所在的包名，多个以逗号分隔。

### register-builtin-request-msg-converters

- 类型：`boolean`
- 默认值：`true`

是否自动注册内置的 `RequestMsgBodyConverter`。

## handler-scan

### enabled

- 类型：`boolean`
- 默认值：`true`

是否启用基于注解的MsgHandler功能。启用后，消息处理可以用 `@Jt808RequestMsgHandlerMapping` 来处理。

使用基于注解的 `MsgHandler` [请参考这里](src/v1/jt-808/guide/annotation-based-dev/msg-handler-register.md)。

### base-packages

- 类型：`String`
- 默认值：`""`

基于注解的 `MsgHandler` 所在的包名，多个以逗号分隔。

### register-builtin-msg-handlers

- 类型：`boolean`
- 默认值：`true`

是否自动注册内置的 `MsgHandler` 。

## exception-handler-scan

### enabled

- 类型：`boolean`
- 默认值：`true`

是否启用全局异常处理机制。启用后，可以用类似于 `Spring` 的全局异常处理的方式来处理异常。

使用基于注解的 `ExceptionHandler` [请参考这里](src/v1/jt-808/guide/annotation-based-dev/exception-handler.md)。

### base-packages

- 类型：`String`
- 默认值：`""`

基于注解的 `ExceptionHandler` 所在的包名，多个以逗号分隔。

### register-builtin-exception-handlers

- 类型：`boolean`
- 默认值：`true`

是否自动注册内置的 `ExceptionHandler` 。

## msg-processor.thread-pool

::: tip
此处为 `消息处理线程池` 相关的配置。其实就是 `Java线程池` 几个关键参数的配置。
:::

### core-pool-size

- 类型：`int`
- 默认值：`Runtime.getRuntime().availableProcessors() + 1`


消息处理线程池的核心线程数，即 `java.util.concurrent.ThreadPoolExecutor.corePoolSize`。

### maximum-pool-size

- 类型：`int`
- 默认值：`2 * corePoolSize`

同 `java.util.concurrent.ThreadPoolExecutor.maximumPoolSize` 。

### keep-alive-time

- 类型：`Duration`
- 默认值：`60s`

同 `java.util.concurrent.ThreadPoolExecutor.keepAliveTime` 。

### blocking-queue-size

- 类型：`int`
- 默认值：`20`

`java.util.concurrent.ThreadPoolExecutor.workQueue` 的 `size()` 。

### thread-name-format

- 类型：`String`
- 默认值：`808-msg-processor-%d`

线程池中线程的命名格式。

## 其他配置

### print-component-statistics

- 类型：`boolean`
- 默认值：`true`

`jt808.print-component-statistics` 表示是否开启服务启动完成后显示组件统计信息。

这些统计信息可以显示已经注册的 `MsgConverter` 、 `MsgHandler` 等组件。类似于下图所示：

<p class="">
    <img :src="$withBase('/img/print-component-statistics.png')" alt="print-component-statistics">
</p> 