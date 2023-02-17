# jt-framework

Jt-808协议服务端。

最新版升级问题，请参考 [升级指南--2.0.x升级到2.1.x](https://hylexus.github.io/jt-framework/v2/jt-808/guide/migration/migrating-from-2.0.x-to-2.1.x.html) 。

最新版升级问题，请参考 [升级指南--2.0.x升级到2.1.x](https://hylexus.github.io/jt-framework/v2/jt-808/guide/migration/migrating-from-2.0.x-to-2.1.x.html) 。

最新版升级问题，请参考 [升级指南--2.0.x升级到2.1.x](https://hylexus.github.io/jt-framework/v2/jt-808/guide/migration/migrating-from-2.0.x-to-2.1.x.html) 。

## Compatibility

支持 **spring-boot-2.x
** [![spring-boot-2.x](https://img.shields.io/maven-central/v/io.github.hylexus.jt/jt-808-server-spring-boot-starter-boot2.svg?label=spring-boot-2.x)](https://search.maven.org/search?q=g:%22io.github.hylexus.jt%22%20AND%20a:%22jt-808-server-spring-boot-starter-boot2%22)
和 **spring-boot-3.x
** [![spring-boot-3.x](https://img.shields.io/maven-central/v/io.github.hylexus.jt/jt-808-server-spring-boot-starter.svg?label=spring-boot-3.x)](https://search.maven.org/search?q=g:%22io.github.hylexus.jt%22%20AND%20a:%22jt-808-server-spring-boot-starter%22) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

两个 `starter` 的 **JDK** 版本、**spring-boot** 版本限制如下：

| Module                                    | JDK   | spring-boot      | Desc                              |
|-------------------------------------------|-------|------------------|-----------------------------------|
| `jt-808-server-spring-boot-starter-boot2` | `11+` | `[2.2.x, 2.6.x]` | 为 **spring-boot-2.x** 提供的 starter |
| `jt-808-server-spring-boot-starter`       | `17+` | `[3.0.0, ...]`   | 为 **spring-boot-3.x** 提供的 starter |

## Modules

本项目默认的 **JDK版本** 和 **spring-boot版本** 见下表(有必要的话,根据实际情况自行调整):

| Module                                  | JDK | CompileLevel | .class      | spring-boot |
|-----------------------------------------|-----|--------------|-------------|-------------|
| jt-808-server-spring-boot-starter       | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.0.2**_ |
| jt-808-server-spring-boot-starter-boot2 | 17  | JDK-11       | 55 (JDK-11) | 2.6.14      |
| jt-808-server-spring-boot-autoconfigure | 17  | JDK-11       | 55 (JDK-11) | 2.6.14      |
| jt-808-server-support                   | 17  | JDK-11       | 55 (JDK-11) | --          |
| jt-808-server-core                      | 17  | JDK-11       | 55 (JDK-11) | --          |

- 模块介绍

```sh
.
├── build-script    # gradle用到的构建脚本和checkstyle配置
├── docs            # 文档 (vue-press)
├── gradle
├── jt-808-server-spring-boot-starter       # spring-boot-v3 自动配置 (jdk17+)
├── jt-808-server-spring-boot-starter-boot2 # spring-boot-v2 自动配置 (jdk11+)
├── jt-808-server-support                   # jt-808的实现
├── jt-core                                 # 一些公共类(后期可能改名为 jt-common)
├── jt-spring                               # 和 spring 集成相关的类(2.x不再依赖,后期可能删除)
└── samples         # 示例项目
    ├── jt-808-server-sample-bare           # 几乎零配置的示例(spring-boot-2.x)
    ├── jt-808-server-sample-bare-boot3     # 几乎零配置的示例(spring-boot-3.x)
    ├── jt-808-server-sample-annotation     # 注解相关的示例(spring-boot-2.x)
    ├── jt-808-server-sample-customized     # 定制化示例(spring-boot-2.x)
    ├── jt-808-client-sample-debug          # 开发时用来Debug的项目,请忽略
    └── jt-808-server-sample-debug          # 开发时用来Debug的项目,请忽略
```

## Docs

- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)

## QuickStart

更多信息，请移步 [入门--快速开始](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/quick-start.html) 。

更多信息，请移步 [入门--快速开始](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/quick-start.html) 。

更多信息，请移步 [入门--快速开始](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/quick-start.html) 。

### 1. 创建工程

创建一个空的 `spring-boot` 工程。

### 2. 添加依赖

关于 `starter` 项目名称的说明:

- **2.1.x** 开始
    - 1). 分为 **spring-boot-2.x** 和 **spring-boot-3.x** 两个版本
    - 2). **RELEASE** 版本命名不带任何后缀(不再以 `-RELEASE` 结尾)
    - 3). 低版本中写错的单词 `stater` 更正为 `starter`
    - 4). **RELEASE** 版本命名规则如下所示:
        - **spring-boot-3.x**
            - `jt-808-server-spring-boot-starter-2.1.0.jar`
        - **spring-boot-2.x**
            - `jt-808-server-spring-boot-starter-boot2-2.1.0.jar`
- **2.1.x** 之前
    - 1). **RELEASE** 版本命名以 `-RELEASE` 结尾
    - 2). 模块名中的单词 `starter` 误写为 `stater`
    - 3). **RELEASE** 版本命名规则如下所示:
        - `jt-808-server-spring-boot-stater-2.0.3-RELEASE.jar`
        - `jt-808-server-spring-boot-stater-2.0.1-RELEASE.jar`
        - ...
        - `jt-808-server-spring-boot-stater-1.0.0-RELEASE.jar`

按需求引入为 `808协议` 提供的 `spring-boot-starter`：

- gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-starter-boot2', version: "2.1.0"
```

- maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter-boot2</artifactId>
    <version>2.1.0</version>
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
- 支持消息下发
- 支持消息分包
- 支持多版本
- 高度可定制

## Building from Source

更多有关编译源码的信息，请移步 [入门--编译源码](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/building-from-source.html) 。

更多有关编译源码的信息，请移步 [入门--编译源码](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/building-from-source.html) 。

更多有关编译源码的信息，请移步 [入门--编译源码](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/building-from-source.html) 。

- Mac/Linux/Unix-Based System

```shell script
./gradlew clean build
```

- Windows

```shell script
./gradlew.bat clean build
```

## Reporting Issues

- **GitHub** 提交 **Issue**
- **QQ**交流群 : **1072477383**

## Community

![QQ交流群](https://hylexus.github.io/jt-framework/img/QQ-Group.jpeg)

## Funding

项目的发展离不开你的支持，请作者喝一杯🍺吧！

![有钱的捧个钱场 没钱的捧个人场](http://img.dferic.com/img/pay.png)
