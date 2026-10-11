---
icon: branch
---

# 3.x

## 3.0.1 (2026-10-11)

### 🐞 Bug Fixes

- 修复 JT/T 808 位置附加项在共享解码器并发首次解析时，可能因字段映射尚未完整初始化而使用错误类型的问题。已配置项和未知项分别使用完整的 key 映射及注解声明的默认描述符 [#103](https://github.com/hylexus/jt-framework/issues/103)
- 修复 `keyValueMappings = {}` 时解析位置附加项可能出现空指针异常的问题；所有附加项按 `defaultKeyValueMapping` 解析
- 修复注解编码器、解码器中自定义转换器实例缓存的并发读写与发布问题，确保并发首次使用时获取已完成构造的实例，保持按转换器类复用实例的既有生命周期

### 🧪 Tests

- 新增 27 项回归测试，覆盖并发首次访问、初始化失败、字段隔离、默认映射、既有注解行为、转换器生命周期和缓冲区释放
- 支持模块的 98 项测试在 Java 8、Java 17 和 Java 21 环境均通过；Spring Boot 2、Spring Boot 3 的相关回归及 starter 兼容性验证通过

### ❤️ Contributors

- Codex
- [@hylexus](https://github.com/hylexus)

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
