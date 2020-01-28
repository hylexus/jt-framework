# 快速开始

此处将展示一个 最少配置 的808协议消息处理服务的搭建。

::: tip
本小节的示例可以在 samples/jt-808-server-sample-bare 下找到对应代码。
:::

## 创建工程

创建一个空的 `spring-boot` 工程。

::: tip
可以使用 [Spring Initializer](https://start.spring.io) 快速初始化一个 Spring Boot 工程。
:::

## 添加依赖

引入为 `808协议` 提供的 `spring-boot-starter`

- gradle

```groovy
dependencies {
	// ...
	implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-stater', version: "1.0-SNAPSHOT"
    // ...
}
```

- maven

```xml
<parent>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-stater</artifactId>
    <version>1.0-SNAPSHOT</version>
    <relativePath/>
</parent>
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

至此，对808消息的处理流程已经搭建完毕。启动 `spring-boot` 项目开始测试。

<p class="">
    <img src="/img/print-component-statistics.png"/>
</p>

可以从启动日志中的`组件统计信息`中看到内置了一个对 `0x0102 (终端鉴权)` 消息的处理器。处理逻辑位于 `io.github.hylexus.jt808.handler.impl.AuthMsgHandler` 。

::: tip
`组件统计信息` 的开关由配置项 `jt808.print-component-statistics = true|false` 来控制
:::

鉴权成功与否是由函数式接口 `io.github.hylexus.jt808.ext.AuthCodeValidator` 处理的。
并且内置了一个 `永远返回true的仅仅用于调试` 的实现类 `BuiltinAuthCodeValidatorForDebugging` 。

### 使用发包工具测试鉴权消息

::: tip
本小节的示例可以在 samples/jt-808-server-sample-bare 下找到对应代码。
:::