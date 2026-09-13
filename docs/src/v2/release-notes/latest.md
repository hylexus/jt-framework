---
icon: branch
---

# 3.x

## 3.0.0 (2026-09-12)

### 🐞 Bug Fixes

- 修复 Netty 消息处理线程池被 Spring 误识别为 `ScheduledExecutorService`，导致 `@Scheduled` 任务可能启动失败的问题 [#101](https://github.com/hylexus/jt-framework/issues/101)
  - 808 指令服务器、808 附件服务器和 1078 服务器的三个具名线程池 Bean 改为 `JtEventExecutorGroupProvider`
  - Bean 名和公开常量保持不变，自定义 Bean 只需调整返回类型和注入类型

### ❤️ Contributors

- Codex
- [@hylexus](https://github.com/hylexus)

## 3.0.0-rc.2(2026-01-27)

### 🐞 Bug Fixes

- 下发指令的消息流水号和应答时的消息流水号对应不上 [#97](https://github.com/hylexus/jt-framework/issues/97)

## 3.0.0-rc.1(2026-01-17)

### ⭐ 新特性

适配 **xtream-codec** [#93](https://github.com/hylexus/jt-framework/issues/93)

- jt-808-server-xtream-codec-adapter
- jt-808-server-xtream-codec-adapter-spring-boot-starter
- jt-808-server-xtream-codec-adapter-spring-boot-starter-boot2
