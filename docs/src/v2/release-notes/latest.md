---
icon: branch
---

# 2.3.x

## 2.3.0-rc.5(2025-04-16)

### ⚡不兼容的变更

- `JtServerNettyConfigure` 及其子接口和实现类
    - `Jt808ServerNettyConfigure`
        - 删除了 ~~DefaultJt808ServerNettyConfigure~~
        - 新增 `BuiltinJt808ServerNettyConfigure`
    - `Jt808AttachmentServerNettyConfigure`
        - 删除了 ~~DefaultJt808AttachmentServerNettyConfigure~~
        - 新增 `BuiltinJt808AttachmentServerNettyConfigure`
- `AbstractRunner` 构造器参数变更

### ⭐ 新特性

- 新增 `DynamicFieldBasedJt808MsgEncoder`
- `Jt808CommandSender` 新增接方法:
    - `sendCommand(Metadata, List<DynamicField>)`
    - `sendCommandWithDynamicFields(Metadata, List<Map<String,Object>>)`
    - `sendCommandAndWaitingForReply(Jt808CommandKey, Metadata, List<DynamicField>, Duration)`
    - `sendCommandWithDynamicFieldsAndWaitingForReply`

### 📖 升级参考

- [文档 - 升级指南 - 升级到2.3.0-rc.5](https://hylexus.github.io/jt-framework/v2/jt-808/guide/migration/upgrade-to-2.3.0-rc.5.html)

## 2.3.0-rc.4(2025-02-24)

### 🐞 Bug Fixes

- `DispatcherHandler` 未释放空消息的问题

## 2.3.0-rc.3(2024-12-11)

### 🐞 Bug Fixes

- 修复 JDK8 环境下启动异常的问题

## 2.3.0-rc.2(2024-11-29)

### ⭐ New Features

新增业务线程池: `jt808.msg-handler`

## 2.3.0-rc.1(2024-08-03)

### ⭐ New Features

下面模块的最低 **JDK** 版本从 `11` 改为 `8`:

- **jt-core**
- **jt-808-server-support**
- **jt-808-server-spring-boot-autoconfigure**
- **jt-808-server-spring-boot-starter-boot2**
- **jt-1078-server-support**
- **jt-1078-server-spring-boot-autoconfigure**
- **jt-1078-server-spring-boot-starter-boot2**

### ❤️ Contributors

- [@MaxonCinker](https://github.com/MaxonCinker)
- [@hylexus](https://github.com/hylexus)
