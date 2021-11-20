# 快速开始

此处将展示一个 最少配置 的808协议消息处理服务的搭建。

::: tip 传送门

本小节的示例可以在 [samples/jt-808-server-sample-bare](https://github.com/hylexus/jt-framework-samples-maven/tree/master/jt-808-server-sample-bare) 下找到对应代码。

:::

## 创建工程

创建一个空的 `spring-boot` 工程。

::: tip 传送门

可以使用 [Spring Initializer](https://start.spring.io) 快速初始化一个 Spring Boot 工程。

:::

## 添加依赖

引入为 `808协议` 提供的 `spring-boot-starter`

- gradle

```groovy
dependencies {
	// ...
	implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-stater', version: "2.0.0-RELEASE"
    // ...
}
```

- maven

```xml
<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-stater</artifactId>
    <version>2.0.0-RELEASE</version>
</dependency>
```

## 配置

- application.yml

```yaml
jt808:
  print-component-statistics: true
logging:
  level: info
  level.io.github.hylexus: debug
```

- `@EnableJt808ServerAutoConfig` 启用自动配置

```java
@SpringBootApplication
// 启用自动配置
@EnableJt808ServerAutoConfig
public class Jt808ServerSampleBareApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jt808ServerSampleBareApplication.class, args);
    }

}
```

## 测试

至此，对 `808` 消息的处理流程已经搭建完毕。启动 `spring-boot` 项目开始测试。

<p class="">
    <img :src="$withBase('/img/v2/print-component-statistics.png')"/>
</p>

可以从启动日志中的 `组件统计信息` 中看到内置了一些默认的消息处理器 : `0X0001 (终端通用应答)`、 `0X0002 (终端心跳)`、 `0X0100 (终端注册)`、 `0X0102 (终端鉴权)` .

::: tip 配置提示

`组件统计信息` 的开关由配置项 `jt808.print-component-statistics = true|false` 来控制

:::

鉴权成功与否是由函数式接口 `io.github.hylexus.jt808.ext.AuthCodeValidator` 处理的。
并且内置了一个 `永远返回true的仅仅用于调试` 的实现类 `BuiltinAuthCodeValidatorForDebugging` 。

### 使用发包工具测试鉴权消息

::: danger 注意：

无论以什么发包工具发包，都请记得以 `十六进制格式` 发送！可以参考 [推荐发包工具](/v2/jt-808/guide/FAQ/debug.md#调试工具) 。

:::

::: danger 注意：

无论以什么发包工具发包，都请记得以 `十六进制格式` 发送！可以参考 [推荐发包工具](/v2/jt-808/guide/FAQ/debug.md#调试工具) 。

:::

::: danger 注意：

无论以什么发包工具发包，都请记得以 `十六进制格式` 发送！可以参考 [推荐发包工具](/v2/jt-808/guide/FAQ/debug.md#调试工具) 。

:::

默认情况下同时支持 `2011` 和 `2019` 两个版本的协议，此处以 `2019` 版的 **终端注册消息** 为例，用发包工具发送如下报文给服务器( `默认TCP端口:6808` )测试：

```
7E010040560100000000013912344321007B000B0000313233353931323335393131323334353637383930313233343536373839303132333435363738393069643132333435363738393031323334353637383930313233343536373801B8CA4A2D3635343332313C7E
```

<p class="">
    <img :src="$withBase('/img/v2/auth-msg-debug.png')"/>
</p>

通过断点可以看到，测试报文被内置的 `BuiltinRegisterRequestMsgV2019HandlerForDebugging` 处理了：

<p class="">
    <img :src="$withBase('/img/v2/auth-msg-breakpoint.png')" alt="auth-msg-breakpoint">
</p>

::: tip 传送门

本小节的示例可以在 [samples/jt-808-server-sample-bare](https://github.com/hylexus/jt-framework-samples-maven/tree/master/jt-808-server-sample-bare) 下找到对应代码。

:::
