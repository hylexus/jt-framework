---
sidebarDepth: 3
---

# 808服务配置

本小节会介绍808服务的配置选项。

默认的配置可以在 
[default-jt808-server-config.yml](https://github.com/hylexus/jt-framework/tree/master/jt-808-server-spring-boot-stater/src/main/resources/META-INF/default-jt808-server-config.yml) 
中查看。
并且已经将默认的配置加入到了 `Spring` 的 `PropertySources` 中，并将其置于最后，名称为 `default-jt808-server-config` 。

<p class="">
    <img :src="$withBase('/img/default-config-property-source.png')" alt="default-config-property-source">
</p> 

## 基本配置

```yaml
jt808:
  print-component-statistics: true
  server:
    # ...
  entity-scan:
    # ...
  msg-processor:
    thread-pool:
      # ...
```

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

## entity-scan

### enabled

- 类型：`boolean`
- 默认值：`true`

是否启用实体扫描功能。启用后，请求报文可以自动映射到使用 `@Jt808ReqMsgBody` 标记的 `请求消息体实体类` 。

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