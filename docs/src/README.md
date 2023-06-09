---
home: true
icon: home
title: 主页
heroText: JT-framework
heroImage: /logo.png
tagline: 基于 spring-boot 的 部标协议 服务端
actions:
  - text: 接入指南
    link: /v2/jt-808/guide/
    type: primary
  - text: 快速开始
    link: /v2/jt-808/guide/quick-start/quick-start
  - text: 配 置
    link: /v2/jt-808/config/
features:
- title: 基于Netty
  details: TCP处理基于Netty 4.x。
  icon: code
- title: Spring-Boot驱动
  details: 提供 Spring-Boot-Starter，开箱即用、可定制化。
  icon: leaf
- title: 简洁至上
  details: 注解驱动开发，省时省力。
  icon: semantic
footer: '<a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">备案号: 沪ICP备2022029946号-2</a >'
---

## 项目结构

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

## 软件版本

::: tip

- JDK : 17+
- Netty : 4.7.x
- VuePress : 2.0.0-beta.61
- Gradle : 7.6

:::
