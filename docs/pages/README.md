---
home: true
# heroImage: /hero.png
heroText: JT-framework
tagline: 808、809(TODO)
actionText: 808 接入指南 →
actionLink: /jt-808/guide/
features:
- title: 基于Netty
  details: TCP处理基于Netty 4.x。
- title: Spring-Boot驱动
  details: 提供 Spring-Boot-Starter，开箱即用、可定制化。
- title: 简介至上
  details: 注解驱动开发，省时省力。
footer: XXX Licensed | Copyright © 2020-present TODO
---

## 项目结构

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

## 致谢

- 本文档基于 [VuePress](https://www.vuepress.cn/) 编写

- `@ExtraMsgBody` 这块的逻辑，感谢这位仁兄[qianhongtang](https://github.com/qianhongtang)的启发

## 软件版本

::: tip
- JDK : 1.8+
- Netty : 4.x
- VuePress : ^1.1.0
- Gradle : 5.5.1
:::