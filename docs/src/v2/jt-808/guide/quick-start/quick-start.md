---
icon: launch
---

# 快速开始

此处将展示一个 最少配置 的 `808协议` 消息处理服务的搭建。

::: tip 传送门

本小节的示例可以在 [samples/jt-808-server-sample-bare][jt-808-server-sample-bare] 下找到对应代码。

:::

## 创建工程

创建一个空的 `spring-boot` 工程。

::: tip 传送门

可以使用 [Spring Initializer](https://start.spring.io) 快速初始化一个 Spring Boot 工程。

:::

## 添加依赖

### spring-boot-2.x

使用 `spring-boot-2.x` 的项目引入为 `808协议` 提供的 `jt-808-server-spring-boot-starter-boot2`

::: code-tabs#gradle

@tab maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter-boot2</artifactId>
    <version>2.3.0-rc.1</version>
</dependency>
```

@tab:active gradle

```groovy
implementation 'io.github.hylexus.jt:jt-808-server-spring-boot-starter-boot2:2.3.0-rc.1'
```

:::

### spring-boot-3.x

使用 `spring-boot-3.x` 的项目引入为 `808协议` 提供的 `jt-808-server-spring-boot-starter`

::: code-tabs#gradle

@tab maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter</artifactId>
    <version>2.3.0-rc.1</version>
</dependency>
```

@tab:active gradle

```groovy
implementation 'io.github.hylexus.jt:jt-808-server-spring-boot-starter:2.3.0-rc.1'
```

:::

## 配置

- application.yml

```yaml
jt808:
  built-components:
    component-statistics:
      enabled: true
    request-handlers:
      enabled: true
logging:
  level.root: info
  level.io.github.hylexus: info
  level.jt-808.request.decoder: debug
  level.jt-808.response.encoder: debug
```

## 测试

### 启动项目

至此，对 **808消息** 的处理服务已经搭建完毕。启动 `spring-boot` 项目开始测试。

<p class="">
    <img :src="$withBase('/img/v2/quick-start/print-component-statistics.jpg')"/>
</p>

可以从启动日志中的`组件统计信息`中看到内置了一些消息处理器：

- `0x0100` 终端注册
- `0x0102` 终端鉴权
- `0x0200` 定位数据上报
- `0x0704` 定位数据批量上报

所以现在可以测试接收内置的这些类型的消息了。下面以 **2019** 版的 **终端注册消息** 为例进行测试：

::: tip 配置提示

- `组件统计信息` 的开关由配置项 `jt808.print-component-statistics = true|false` 来控制
- 默认的 `TCP` 端口为 `6808`

:::

### 发报文

::: danger

注意：

- 无论以什么发包工具发包，都请记得以 `十六进制格式` 发送！可以参考 [推荐发包工具](../../../../frequently-asked-questions/debug.md) 。

:::

- 报文

```
[7E010040560100000000013912344321007B000B0000313233353931323335393131323334353637383930313233343536373839303132333435363738393069643132333435363738393031323334353637383930313233343536373801B8CA4A2D3635343332313C7E]
```

- 客户端

<p class="">
    <img :src="$withBase('/img/v2/quick-start/register-msg-2019-client.png')"/>
</p>

### 服务端

<p class="">
    <img :src="$withBase('/img/v2/quick-start/register-msg-2019-breakpoint.png')"/>
</p>

::: tip 传送门

本小节的示例可以在 [samples/jt-808-server-sample-bare][jt-808-server-sample-bare] 下找到对应代码。

:::

[jt-808-server-sample-bare]: https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-bare  "jt-808-server-sample-bare"

