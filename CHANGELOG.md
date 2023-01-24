## 2.1.0-RC1

### ⭐ New Features

从 **2.1.x** 开始，同时支持 **spring-boot-2.x** 和 **spring-boot-3.x**。

提供的 `starter` 的 **JDK** 版本、**spring-boot** 版本限制如下：

| Module                                    | JDK   | spring-boot      | Desc                              |
|-------------------------------------------|-------|------------------|-----------------------------------|
| `jt-808-server-spring-boot-starter-boot2` | `11+` | `[2.2.x, 2.6.x]` | 为 **spring-boot-2.x** 提供的 starter |
| `jt-808-server-spring-boot-starter`       | `17+` | `[3.0.0, ...]`   | 为 **spring-boot-3.x** 提供的 starter |

变更如下:

- 1). 模块名称拼写错误修改
    - **2.0.x** 中的 `jt-808-server-spring-boot-stater` 有单词拼写错误(😂): **starter** 写成了 **stater**
    - 在 **2.1.x** 中改成了 `jt-808-server-spring-boot-starter-boot2`
        - **stater** 修改为 **starter**
        - 添加了 **-boot2** 后缀，表示这个模块是给 **spring-boot-2.x** 提供的
- 2). **JDK版本** 和 **spring-boot版本** 修改
    - `jt-808-server-spring-boot-starter`
        - 给 **spring-boot-3.x** 的项目提供的，是本次新增的模块
        - `JDK`: **17**
        - 编译之后的 `.class` 文件版本 : **61**(**JDK-17**)
        - `spring-boot`: **3.0.2**
        - 单独依赖该模块时必须满足: `jdk.version >=17 && spring-boot.version >= 3.0.0`
    - `jt-808-server-spring-boot-starter-boot2`
        - 给 **spring-boot-2.x** 的项目提供的，是从之前的 `jt-808-server-spring-boot-stater` 重命名过来的
        - `JDK`: **17**
        - 编译之后的 `.class` 文件版本 : **55**(**JDK-11**)
        - `spring-boot`: **2.6.14**
        - 单独依赖该模块时必须满足: `jdk.version >= 11 && spring-boot.version >= 2.2.x && spring-boot.version <= 2.6.x`
- 3). **spring-boot版本** 升级
    - 从 **2.5.12** 升级到 **2.6.24**
    - 新增了 **spring-boot-3.x** 的支持

### 🔨 Dependency Upgrades

- `Gradle` 版本升级到 **7.6**
- `Spring-Boot` 版本升级到 **2.6.14**

## 2.0.3-RELEASE

## 2.0.3-rc1

### ⭐ New Features

- `Jt808Session` 新增如下方法:
    - `setAttribute`
    - `getAttribute`
    - `getRequiredAttribute`
    - `removeAttribute`
- `Jt808ServerExchange` 新增如下方法
    - `removeAttribute`

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/66
- 配置项默认值调整:

```yaml
jt808:
  server:
    idle-state-handler:
      # 改动原因见 https://github.com/hylexus/jt-framework/issues/66
      writer-idle-time: 0s # 由 20m 改为 0s(disabled)
      all-idle-time: 0s # 由 20m 改为 0s(disabled)
```

## 2.0.2-RELEASE

### ⭐ New Features

- 新增 `Jt808RequestLifecycleListener`

### 📔 Documentation

- 新增 **Jt808RequestLifecycleListener** 文档
- 新增 **辅助工具** 文档

### ❤️ Contributors

- [@hylexus](https://github.com/hylexus)

## 2.0.2-rc2

### ⭐ New Features

- 新增 `Jt808MsgBuilder`
- 新增 `ByteArrayFieldSerializer`

### 🔨 Dependency Upgrades

- `Spring-Boot` 版本升级到 **2.5.12**

### ❤️ Contributors

- [@hylexus](https://github.com/hylexus)

## 2.0.2-rc1

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/64

### ⭐ New Features

- 去掉 `EventBus`，业务线程池使用自定义的 `EventExecutorGroup`
- 废弃 `jt808.msg-processor.thread-pool.xxx` 配置项，使用 `jt808.msg-processor.executor-group.xxx` 代替

### 🔨 Dependency Upgrades

- `Gradle` 版本升级到 **6.8.1**
- `Spring-Boot` 版本升级到 **2.5.7**
- `Netty` 版本升级到 **4.1.75.Final**

### ❤️ Contributors

- [@hylexus](https://github.com/hylexus)

## 2.0.1-RELEASE

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/63

## 2.0.0-RELEASE

### ⭐ New Features

- 注解驱动开发(支持`SpEL`)
- 支持消息分包
- 支持多版本

### 📔 Documentation

新增 **2.x** 文档。

### ❤️ Contributors

- [@dfEric](https://github.com/dfEric)
- [@hylexus](https://github.com/hylexus)

# 1.0.12-RELEASE

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/44

### ⭐ New Features

新增配置项：

- `jt808.server.idle-state-handler` : 对应 `io.netty.handler.timeout.IdleStateHandler` 的 三个属性：`readerIdleTime`、 `writerIdleTime` 、`allIdelTime`。 默认值都是 `20m`;
- `jt808.protocol.max-frame-length` : 对应 `io.netty.handler.codec.DelimiterBasedFrameDecoder` 的 `maxFrameLength` 属性。

# 1.0.11-RELEASE

### ⭐ New Features

新增 `io.github.hylexus.jt.msg.builder.jt808.Jt808MsgBuilder`

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/32
- https://github.com/hylexus/jt-framework/issues/33
- https://github.com/hylexus/jt-framework/issues/34
- https://github.com/hylexus/jt-framework/issues/37

# 1.0.10-RELEASE

### ⭐ New Features

- https://github.com/hylexus/jt-framework/pull/28
    - @Jt808RequestMsgConverter
    - @Jt808RequestMsgHandler

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/26

# 1.0.9-SNAPSHOT

### ⭐ New Features

- `@NestedFieldMapping` 支持List类型 (https://github.com/hylexus/jt-framework/issues/25)

### 🐞 Bug Fixes

### 🔨 Others

- 文档更新

# 1.0.8-SNAPSHOT

### ⭐ New Features

- 支持2019版: [`jt808.protocol.version`](https://hylexus.github.io/jt-framework/jt-808/config/#version)

### 🐞 Bug Fixes

- `@NestedFieldMapping` bug

### 🔨 Others

- 文档更新

# 1.0.7-SNAPSHOT

### ⭐ New Features

- 修改自定义配置的方式
    - 旧版的`Jt808ServerConfigure` 容易引发Bean的循环依赖
    - 从1.0.7-RELEASE开始，使用 `Jt808ServerConfigurationSupport` 来自定义配置
- Maven依赖的 `scope` 从 `runtime` 改为 `compile`，涉及到的依赖如下：
    - Netty
    - oaks-lib

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/18
- BytesEncoder转义bug

### 🔨 Others

- Jt808Session#setChannel
- BytesEncoder支持校验码逻辑自定义

# 1.0.5-SNAPSHOT

### ⭐ New Features

- `Jt808SessionManager` 重构，支持自定义

### 🐞 Bug Fixes

- SessionManager#removeBySessionIdAndClose 的bug

### 🔨 Others

- `CommandSender` 文档完善

# 1.0.4-SNAPSHOT

### ⭐ New Features

- `MsgHandler` 可选择性地交给Spring管理，以便于在 `MsgHandler` 中调用其他的由Spring管理的Bean

### 🐞 Bug Fixes

- https://github.com/hylexus/jt-framework/issues/16
    - 默认读写空闲时间改为20分钟
    - 修复示例代码中消息消息的返回值类型
- https://github.com/hylexus/jt-framework/issues/17
    - 边界情况下默认转义逻辑bug

### 🔨 Others

- 优化 `Session#currentFlowId`
- 新增 `SessionManager#findBySessionId`

# 1.0.3-SNAPSHOT

- `MsgHandler` 可选择性地交给Spring管理，以便于在 `MsgHandler` 中调用其他Bean
- bug-fix: #16,#17

## Bug Fixes

* **decoder:** 嵌套类型丢失数据 (#b1a8eaca, closes #2)

## 2020-02-09

### Other Changes

- docs(todo-list): README.md,CHANGELOG.md
- ExceptionHandler

## 2020-02-08

### Other Changes

- tmp

## 2020-02-06

### Other Changes

- docs

## 2020-02-05

### Other Changes

- CustomReflectionBasedRequestMsgBodyConverter

## 2020-02-03

### Other Changes

- docs

## 2020-02-02

### Other Changes

- docs
- docs
- OrderedComponent
- HandlerMethodArgumentResolver

## 2020-02-01

### Other Changes

- ReflectionBasedRequestMsgHandler
- maven-config

## 2020-01-31

### Other Changes

- README.md
- docs
- docs
- deploy docs
- deploy docs
- deploy docs
- docs

## 2020-01-30

### Other Changes

- tmp
- docs
- Jt808DefaultEnvironmentPostProcessor

## 2020-01-29

### Other Changes

- docs
- docs

## 2020-01-28

### Other Changes

- tmp
- docs
- BytesEncoder
- docs
- docs

## 2020-01-27

### Other Changes

- docs

## 2020-01-26

### Other Changes

- @EnableJt808ServerAutoConfig
- samples
- samples

## 2020-01-25

### Other Changes

- tmp

## 2019-10-28

### Other Changes

- tmp

## 2019-10-22

### Other Changes

- tmp

## 2019-10-21

### Other Changes

- tmp

## 2019-10-17

### Other Changes

- tmp

## 2019-10-16

### Other Changes

- tmp

## 2019-10-13

### Other Changes

- CommandWaitingPool

## 2019-10-09

### Other Changes

- tmp

## 2019-10-06

### Other Changes

- @SlicedFrom

## 2019-10-04

### Other Changes

- oaks-lib --> 1.0.4
- DataType
- @SliceFrom
- SplittableField

## 2019-10-03

### Other Changes

- 附加消息解析

## 2019-10-01

### Other Changes

- init vue-press
- rename 'jt-platform' to 'jt-framework'
- 扁平化解析附加消息

## 2019-09-29

### Other Changes

- 重构消息解析代码

## 2019-09-25

### Other Changes

- tmp

## 2019-09-22

### Other Changes

- RequestMsgHeaderAware
- tmp

## 2019-09-18

### Other Changes

- tmp

## 2019-09-17

### Other Changes

- delete LocalMsgQueue

## 2019-08-29

### Other Changes

- boot-auto-configure

## 2019-08-28

### Other Changes

- boot-auto-configure
- auto-configure
- component-scan

## 2019-08-27

### Other Changes

- auto config

## 2019-08-26

### Other Changes

- init spring-boot-starter

## 2019-08-25

### Other Changes

- tmp

## 2019-08-24

### Other Changes

- bug-fix
- sessionManager
- sessionManager
- eventBus

## 2019-08-22

### Other Changes

- MsgDispatcher

## 2019-08-21

### Other Changes

- migrate to gradle

## 2019-07-07

### Other Changes

- tmp

## 2019-07-06

### Other Changes

- init

