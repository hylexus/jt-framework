# jt-framework

Jt-808协议服务端。

最新版升级问题，请参考 [升级指南--2.0.x升级到2.1.x](https://hylexus.github.io/jt-framework/v2/jt-808/guide/migration/migrating-from-2.0.x-to-2.1.x.html) 。

## 私有协议编解码小工具推荐

不依赖具体协议的私有协议编解码小工具。

- Github : https://github.com/hylexus/xtream-codec
- Gitee : https://gitee.com/hylexus/xtream-codec

## Compatibility

支持 **spring-boot-2.x** [![spring-boot-2.x](https://img.shields.io/maven-central/v/io.github.hylexus.jt/jt-808-server-spring-boot-starter-boot2.svg?label=spring-boot-2.x)](https://search.maven.org/search?q=g:%22io.github.hylexus.jt%22%20AND%20a:%22jt-808-server-spring-boot-starter-boot2%22)
和 **spring-boot-3.x** [![spring-boot-3.x](https://img.shields.io/maven-central/v/io.github.hylexus.jt/jt-808-server-spring-boot-starter.svg?label=spring-boot-3.x)](https://search.maven.org/search?q=g:%22io.github.hylexus.jt%22%20AND%20a:%22jt-808-server-spring-boot-starter%22) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

`starter` 的 **JDK** 版本、**spring-boot** 版本限制如下：

| Module                                     | JDK   | spring-boot       | Desc                                       |
|--------------------------------------------|-------|-------------------|--------------------------------------------|
| `jt-808-server-spring-boot-starter-boot2`  | `8+`  | `[2.2.x, 2.7.18]` | 为 **spring-boot-2.x** 提供的 starter          |
| `jt-808-server-spring-boot-starter`        | `17+` | `[3.0.0, ...]`    | 为 **spring-boot-3.x** 提供的 starter          |
| `jt-1078-server-spring-boot-starter-boot2` | `8+`  | `[2.2.x, 2.7.18]` | 为 **spring-boot-2.x** 提供的 starter; `beta版` |
| `jt-1078-server-spring-boot-starter`       | `17+` | `[3.0.0, ...]`    | 为 **spring-boot-3.x** 提供的 starter; `beta版` |
| `jt-dashboard-client-spring-boot-starter`  | `17+` | `[3.0.0, ...]`    | 暂时只支持 `jdk17+/spring-boot-3.x`; `beta版`    |
| `jt-dashboard-server-spring-boot-starter`  | `17+` | `[3.0.0, ...]`    | 暂时只支持 `jdk17+/spring-boot-3.x`; `beta版`    |

## Modules

本项目默认的 **JDK版本** 和 **spring-boot版本** 见下表(有必要的话,根据实际情况自行调整):

| Module                                   | JDK | CompileLevel | .class      | spring-boot | Desc                                      |
|------------------------------------------|-----|--------------|-------------|-------------|-------------------------------------------|
| jt-core                                  | 17  | JDK-8        | 52 (JDK-8)  | --          |                                           |
| jt-808-server-spring-boot-starter        | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.3.0**_ |                                           |
| jt-808-server-spring-boot-starter-boot2  | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-808-server-spring-boot-autoconfigure  | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-808-server-support                    | 17  | JDK-8        | 52 (JDK-8)  | --          |                                           |
| jt-1078-server-spring-boot-starter       | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.3.0**_ |                                           |
| jt-1078-server-spring-boot-starter-boot2 | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-1078-server-spring-boot-autoconfigure | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-1078-server-support                   | 17  | JDK-8        | 52 (JDK-8)  | --          |                                           |
| `dashboard/**`                           | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.3.0**_ | dashboard 模块暂时只支持 `spring-boot-3.x/jdk17` |

- 模块介绍

```sh
.
├── build-script   # gradle 用到的构建脚本和checkstyle配置
├── docs  # 文档 (vue-press)
├── jt-1078-server-spring-boot-autoconfigure  # (jdk8+)
├── jt-1078-server-spring-boot-starter        # spring-boot-v3 自动配置 (jdk17+)
├── jt-1078-server-spring-boot-starter-boot2  # spring-boot-v2 自动配置 (jdk8+)
├── jt-1078-server-support                    # jt-1078 的实现 (jdk8+)
├── jt-808-server-spring-boot-autoconfigure   # (jdk8+)
├── jt-808-server-spring-boot-starter         # spring-boot-v3 自动配置 (jdk17+)
├── jt-808-server-spring-boot-starter-boot2   # spring-boot-v2 自动配置 (jdk8+)
├── jt-808-server-support                     # jt-808 的实现 (jdk8+)
├── jt-core                                   # 一些公共类 (jdk8+)
├── dashboard # dashboard 模块 「暂时」 不支持 spring-boot-2.x
│     ├── jt-dashboard-client                       # dashboard 客户端 (jdk17+)
│     ├── jt-dashboard-client-spring-boot-starter   # dashboard 客户端的 spring-boot-v3 自动配置 (jdk17+)
│     ├── jt-dashboard-common                       # dashboard 公共依赖 (jdk17+)
│     ├── jt-dashboard-server                       # dashboard 服务端 (jdk17+)
│     ├── jt-dashboard-server-spring-boot-starter   # dashboard 服务端的 spring-boot-v3 自动配置 (jdk17+)
│     └── jt-dashboard-server-ui-vue3               # dashboard 的 UI(基于 VUE3)
├── demos # (jdk17+)
│     ├── jt-demo-1078-server-webflux-boot3
│     ├── jt-demo-808-server-webflux-boot3
│     ├── jt-demo-dashboard-webflux-boot3
│     └── jt-demo-dashboard-webmvc-boot3
└── samples
      ├── jt-1078-server-sample-webflux-boot3 # JT/T 1078 的示例(spring-boot-3.x; Webflux) (jdk17+)
      ├── jt-1078-server-sample-webmvc-boot3  # JT/T 1078 的示例(spring-boot-3.x; SpringMVC) (jdk17+)
      ├── jt-808-server-sample-annotation     # 注解相关的示例(spring-boot-2.x) (jdk8+)
      ├── jt-808-server-sample-bare           # 几乎零配置的示例(spring-boot-2.x) (jdk8+)
      ├── jt-808-server-sample-bare-boot3     # 几乎零配置的示例(spring-boot-3.x) (jdk17+)
      ├── jt-808-server-sample-customized     # 定制化示例(spring-boot-2.x) (jdk8+)
      ├── jt-808-client-sample-debug          # 开发时用来Debug的项目,请忽略
      ├── jt-808-server-sample-debug          # 开发时用来Debug的项目,请忽略
      └── jt-sample-common                    # samples 项目共同的依赖 (jdk8+)

```

## Docs

- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)

## QuickStart

更多信息，请移步 [入门--快速开始](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/quick-start.html) 。

### 1. 创建工程

创建一个空的 `spring-boot` 工程。

### 2. 添加依赖

- **spring-boot-3.x**
    - `jt-808-server-spring-boot-starter-2.3.0-rc.1.jar`
- **spring-boot-2.x**
    - `jt-808-server-spring-boot-starter-boot2-2.3.0-rc.1.jar`

按需求引入为 `808协议` 提供的 `spring-boot-starter`：

- gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-starter-boot2', version: "2.3.0-rc.1"
```

- maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter-boot2</artifactId>
    <version>2.3.0-rc.1</version>
</dependency>
```

### 3. 配置

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

## Features

- 注解驱动开发
- 支持苏标附件服务器
- 支持消息下发
- 支持消息分包
- 支持多版本
- 支持报文加解密

## Maven Samples

Maven版示例项目

- Github: [https://github.com/hylexus/jt-framework-samples-maven](https://github.com/hylexus/jt-framework-samples-maven)
- Gitee: [https://gitee.com/hylexus/jt-framework-samples-maven](https://gitee.com/hylexus/jt-framework-samples-maven)

## Building from Source

更多有关编译源码的信息，请移步 [入门--编译源码](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/building-from-source.html) 。

- Mac/Linux/Unix-Based System

```shell script
./gradlew clean build
```

- Windows

```shell script
.\gradlew.bat clean build
```

## Reporting Issues

- **GitHub** 提交 **Issue**
- **QQ**交流群 : **1072477383**

## Community

![QQ交流群](https://hylexus.github.io/jt-framework/img/QQ-Group.jpeg)

## Funding

项目的发展离不开你的支持，请作者喝一杯🍺吧！

![有钱的捧个钱场 没钱的捧个人场](https://hylexus.github.io/jt-framework/img/pay.png)

