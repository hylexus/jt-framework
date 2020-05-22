# jt-framework

Jt-808协议服务端。

## Docs

- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)

## ChangeLog

- `maven` 版 `jt-808-server-spring-boot-stater`的 `scope` 改为 `compile`
- `MsgDataType` 新增 `List` 类型
- Handler返回类型支持 `@Jt808RespMsgBody` 注解
- 支持下发消息

## QuickStart

### 1. 创建工程

创建一个空的 `spring-boot` 工程。

### 2. 添加依赖

引入为 `808协议` 提供的 `spring-boot-starter`

- gradle

```groovy
dependencies {
	// ...
	implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-stater', version: "1.0.3-RELEASE"
    // ...
}
```

- maven

```xml
<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-stater</artifactId>
    <version>1.0.3-RELEASE</version>
</dependency>
```

### 3. 配置

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

## Features

- [注解驱动开发](https://hylexus.github.io/jt-framework/jt-808/guide/annotation-based-dev/)
    - 基于注解的请求消息映射
    - 基于注解的Handler，参数自动注入
    - 基于注解的响应消息映射
    - 基于注解的消息下发
- 支持消息下发
- [定制化](https://hylexus.github.io/jt-framework/jt-808/guide/basic/customized.html#application-yml)
    - BytesEncoder：自定义转义逻辑
    - RequestMsgDispatcher：自定义 `MsgDispatcher`
    - RequestMsgQueue：自定义 `MsgQueue`，内置基于 `Google-EventBus` 的 MsgQueue
    - RequestMsgQueueListener：自定义 `MsgQueueListener`，内置基于 `Google-EventBus` 的 MsgQueueListener
    - ResponseMsgBodyConverter：自定义消息转换器
    - HandlerMethodArgumentResolver：自定义参数解析器，类似于SpringMVC的 `HandlerMethodArgumentResolver`
    - ……

## Building from Source

- Mac/Linux/Unix-Based System

```shell script
./gradlew clean build
```

- Windows

```shell script
./gradlew.bat clean build
```

文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)

## Modules

```sh
.
├── build-script    # gradle用到的构建脚本和checkstyle配置
├── docs            # 文档 (vue-press)
├── gradle
├── jt-808-server-spring-boot-stater
├── jt-808-server-support
├── jt-core
├── jt-spring
└── samples         # 示例项目
    ├── jt-808-server-sample-bare           # 几乎零配置的示例
    ├── jt-808-server-sample-annotation     # 注解相关的示例
    └── jt-808-server-sample-customized     # 定制化示例
```

## Reporting Issues

- GitHub提交Issue
- QQ交流群

## Community

![QQ交流群](https://hylexus.github.io/jt-framework/img/QQ-Group.jpeg)