# jt-framework

Jt-808协议服务端。

支持 `spring-boot-2.x` 和 `spring-boot-3.x` 。

## Modules

- `JDK` 和 `spring-boot` 版本要求

| 模块                                        | spring-boot(默认) | JDK     | spring-boot(最低)                |
|-------------------------------------------|-----------------|---------|--------------------------------|
| `jt-808-server-spring-boot-starter`       | 3.0.2           | **17+** | `spring-boot.version >= 3.0.0` |
| `jt-808-server-spring-boot-starter-boot2` | 2.6.14          | 11+     | `spring-boot.version >= 2.2.x` |
| `jt-808-server-support`                   | 2.6.14          | 11+     | --                             |
| `jt-core`                                 | --              | 11+     | --                             |

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
    ├── jt-808-server-sample-bare           # 几乎零配置的示例
    ├── jt-808-server-sample-annotation     # 注解相关的示例
    ├── jt-808-server-sample-customized     # 定制化示例
    └── jt-808-server-sample-debug          # 开发时用来Debug的项目,请忽略
```

## Docs

- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
- 文档请移步：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)

## QuickStart

### 1. 创建工程

创建一个空的 `spring-boot` 工程。

### 2. 添加依赖

引入为 `808协议` 提供的 `spring-boot-starter`

- gradle

```groovy
dependencies {
    // ...
    implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-stater', version: "2.0.3-rc1"
    // ...
}
```

- maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-stater</artifactId>
    <version>2.0.3-rc1</version>
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

编译所需的 `JDK` 版本必须在 `17` (包括)以上。

编译之后生成的 `.class` 文件：

[jt-808-server-spring-boot-starter](jt-808-server-spring-boot-starter) 模块为 `JDK17`；其余模块都为 `JDK11`。

[jt-808-server-spring-boot-starter](jt-808-server-spring-boot-starter) 模块为 `JDK17`；其余模块都为 `JDK11`。

[jt-808-server-spring-boot-starter](jt-808-server-spring-boot-starter) 模块为 `JDK17`；其余模块都为 `JDK11`。

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
