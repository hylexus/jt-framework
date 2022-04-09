# jt-framework

Jt-808协议服务端。

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
    implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-stater', version: "2.0.2-RELEASE"
    // ...
}
```

- maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-stater</artifactId>
    <version>2.0.2-RELEASE</version>
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

请事先安装 `Lombok` 插件。

- Mac/Linux/Unix-Based System

```shell script
./gradlew clean build
```

- Windows

```shell script
./gradlew.bat clean build
```

## Modules

```sh
.
├── build-script    # gradle用到的构建脚本和checkstyle配置
├── docs            # 文档 (vue-press)
├── gradle
├── jt-808-server-spring-boot-stater        # spring-boot 自动配置
├── jt-808-server-support                   # jt-808的实现
├── jt-core                                 # 一些公共类(后期可能改名为 jt-common)
├── jt-spring                               # 和 spring 集成相关的类(2.x不再依赖,后期可能删除)
└── samples         # 示例项目
    ├── jt-808-server-sample-bare           # 几乎零配置的示例
    ├── jt-808-server-sample-annotation     # 注解相关的示例
    ├── jt-808-server-sample-customized     # 定制化示例
    └── jt-808-server-sample-debug          # 开发时用来Debug的项目,请忽略
```

## Reporting Issues

- **GitHub** 提交 **Issue**
- **QQ**交流群 : **1072477383**

## Community

![QQ交流群](https://hylexus.github.io/jt-framework/img/QQ-Group.jpeg)
