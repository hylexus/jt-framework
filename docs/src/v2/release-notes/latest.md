---
icon: branch
---

# 2.1.x

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

## 2.1.5(2024-07-20)

### ⭐ New Features

`BitOperator` 新增方法:

- `mapIf(...)`
- `setIf(...)`
- `setWithStatus(...)`
- `setWithStatusIf(...)`
- `setRangeIf(...)`
- `resetIf(...)`
- `resetRangeIf(...)`
- `unsignedLongValue(...)`
- `rangedUnsignedIntValue(...)`
- `rangedUnsignedLongValue(...)`

### 🐞 Bug Fixes

`BitOperator` 操作 `offset >= 31` 的 `bit` 时溢出的问题，涉及到的方法如下：

- `BitOperator#set(int offset)`
- `BitOperator#reset(int offset)`
- `BitOperatorget(int offset)`

### 🔨 Dependency Upgrades

- `oaks-common-utils` 升级到 **1.0.7**

## 2.1.4(2024-06-08)

### ⭐ New Features

- 完善 `Jt808MsgBuilder`
- 新增 `RebuildableByteBufJt808MsgBuilder`

### 🔨 Dependency Upgrades

- `Gradle` : **8.6** 升级到 **8.8**
- `spring-boot-dependencies`
    - **2.7.14** 升级到 **2.7.18**
    - **3.1.2** 升级到 **3.3.0**
- `spring-cloud-dependencies`
    - **2021.0.8** 升级到 **2021.0.9**
    - **2022.0.4** 升级到 **2023.0.2**

### 📔 Documentation

- 新增消息加解密相关文档
- 新增 `RebuildableByteBufJt808MsgBuilder` 相关文档

## 2.1.4-rc.4(2024-06-02)

### ⭐ New Features

初步支持消息加解密，详情见: [#82](https://github.com/hylexus/jt-framework/issues/82)

- 新增 `Jt808MsgEncryptionHandler`
- `@Jt808ResponseBody` 新增 `encryptionType(int)` 属性
- `Jt808Response` 新增 `encryptionType(int)` 属性
- `Jt808MsgBuilder` 新增 `encryptionType(int)` 属性

## 2.1.4-rc.3(2024-03-24)

### 🐞 Bug Fixes

- 解决 `ExtendedJt808FieldDeserializerLocationExtraItem` 没有考虑 `length` 属性的问题

## 2.1.4-rc.2(2024-01-28)

### ⭐ New Features

- 优化苏标附件服务器的支持
- 支持位置附加项列表注解 `@RequestFieldAlias.LocationMsgExtraItemMapping()`
- 新增 `@RequestField#conditionalOn()` 属性
- 内置几个和苏标相关的实体类

## 2.1.4-beta1(2024-01-14)

### ⭐ New Features

- 支持苏标附件服务器

## 2.1.3(2023-10-05)

### 🐞 Bug Fixes

- `BuiltinCommonHandler.processTerminalHeartBeatMsg` 应该回复 `0x8001` 消息，而不是不回复消息

## 2.1.2(2023-09-01)

### ⭐ New Features

- 参见 [#78](https://github.com/hylexus/jt-framework/issues/78)

## 2.1.1(2023-05-12)

### ⭐ New Features

参见 **2.1.1-RC1**、**2.1.1-RC2**、**2.1.1-RC3** 中的所有新特性。

### 🐞 Bug Fixes

- `jt808.features.program-param-printer.enabled = false` 的时候 **classpath** 下没有 `Jackson` 依赖时报错的问题。

## 2.1.1-RC3(2023-05-06)

### ⭐ New Features

- 完善 `BitOperator` 辅助类
- `@SlicedFrom` 支持 `long` 类型

## 2.1.1-RC2(2023-05-02)

### ⭐ New Features

对 **2.1.1-RC1** 中新增特性的简化。

## 2.1.1-RC1(2023-04-29)

### ⭐ New Features

- 支持通过 **注解别名** 来扩展`@RequestField` 和 `@ResponseField`
    - 并内置了 `@RequestFieldAlias` 和 `@ResponseFieldAlias` 别名
    - 现在你可以自定义注解来扩展自定义的数据类型，比如可以自定义一种 `LWord`(`LongWord`,`64bit`) 类型
- 扩展注解别名: `@RequestFieldAlias` 和 `@ResponseFieldAlias`
    - 支持 `MsgDataType#BYTES` 和 `java.lang.String` 互转
    - `Geo` 类型(经纬度)支持转换为 `long/Long`、`double/Double`、`BigDecimal`
    - 表达时间语义的 `BCD` 可以转换为 `java.lang.String`、`java.util.Date`、`java.time.LocalDateTime`
    - `java.util.Date` 和 `java.time.LocalDateTime` 可以转为 `BCD`
- `Jt808FieldDeserializer` 扩展 `Context` 参数，方便自定义注解
- `Jt808FieldSerializer` 扩展 `Context` 参数，方便自定义注解
- 新增 `Jt808RequestFilter`(需要配置 `jt808.features.request-filter.enabled=true`) 支持
- 新增 `BitOperator`, 支持将 `WORD`, `DWORD`, `BYTE` 反序列化为 `BitOperator`
- 废弃 `BytesValueWrapper`
    - 使用 `ByteBufContainer` 或 `ByteArrayContainer` 代替
    - 内置 `ByteBufContainer` 或 `ByteArrayContainer` 的类型转换器
- 新增如下示例消息(按需修改)
    - `BuiltinMsg8103`
    - `BuiltinMsg8103Alias`
    - `BuiltinMsg8100Alias`
    - `BuiltinMsg8300`
    - `BuiltinMsg8300Alias`
    - `BuiltinMsg8600V2011Alias`
    - `BuiltinMsg8600V2013Alias`
    - `BuiltinMsg8600V2019Alias`
    - `BuiltinMsg8602V2011Alias`
    - `BuiltinMsg8602V2013Alias`
    - `BuiltinMsg8602V2019Alias`
    - `BuiltinMsg8604V2011Alias`
    - `BuiltinMsg8604V2013Alias`
    - `BuiltinMsg8604V2019Alias`

### 🐞 Bug Fixes

- `@RequestField#length()` 支持返回 `0`
- 内置的空的心跳消息 `BuiltinMsg0200` 改名为 `BuiltinMsg0002`(之前命名错误)

### 🔨 Others

- `jt808.msg-processor.executor-group.thread-count` 默认值改为 `128`
- 新增配置项 `jt808.features.program-param-printer.enabled`

## 2.1.0

### ⭐ New Features

- 参见 **2.1.0-RC2** 和 **2.1.0-RC1** 的改动记录
- `settings.gradle` 中删除 `docs` 子模块(无需 gradle 管理)

## 2.1.0-RC2

### ⭐ New Features

- **jt-808-server-spring-boot-starter** 模块
    - 排除 **jt-808-server-spring-boot-autoconfigure** 中自带的 **2.x** 版的 **spring-boot**
    - 显式引入 **3.x** 版的 **spring-boot**

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

