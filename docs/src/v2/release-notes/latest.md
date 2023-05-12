---
icon: branch
---

# 2.1.x

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

