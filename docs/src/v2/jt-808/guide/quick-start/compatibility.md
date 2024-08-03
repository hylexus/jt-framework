---
icon: plugin
---

# 兼容性

## 请先读我

::: danger

- **2.1.x** 以下的版本
    - 只有一个为 **spring-boot-2.x** 提供的 `jt-808-server-spring-boot-stater`
    - 并且模块名中的 **starter** 单词拼写错误(写成了 `stater`)
    - 并没有为 `spring-boot-3.x` 提供 `starter`
    - 实际上，至少在 **spring-boot-3.0.1** 中还是能正常解析 **spring-boot-2.x** 的 `starter` 的
- **2.1.x** 开始，提供了两个 `starter`
    - `jt-808-server-spring-boot-starter` 为 **spring-boot-3.x** 提供的 `starter`
    - `jt-808-server-spring-boot-starter-boot2` 为 **spring-boot-2.x** 提供的 `starter`
    - 后续版本会以 **spring-boot-3.x** 为主，同时尽量兼容 **spring-boot-2.x**
- **2.3.x** 开始
    - `jt-808-server-spring-boot-starter-boot2` 支持 **JDK-8+**
    - 之前版本只支持 **JDK-11+**

:::

## 项目模块介绍

默认 **JDK版本** 和 **spring-boot版本** 见下表:

| Module                                  | JDK | CompileLevel | .class      | spring-boot |
|-----------------------------------------|-----|--------------|-------------|-------------|
| jt-808-server-spring-boot-starter       | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.3.0**_ |
| jt-808-server-spring-boot-starter-boot2 | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |
| jt-808-server-spring-boot-autoconfigure | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |
| jt-808-server-support                   | 17  | JDK-8        | 52 (JDK-8)  | --          |
| jt-808-server-core                      | 17  | JDK-8        | 52 (JDK-8)  | --          |

## JDK

- 项目源码要求的 **JDK版本** 为 `JDK17+`
- 但是编译之后的 `jar` 包中 `.class` 文件最低要求为 `JDK8+`

::: info 提示

也就是说：

- 如果你直接在 **本项目源码** 上进行业务开发(不推荐)
    - 必须满足 `java.version >= 17`
- 如果你是将本项目发布的 **jar文件** 依赖到你的项目里
    - 基于 **spring-boot-2.x** 的项目, 满足 `java.version >= 8` 即可
    - 基于 **spring-boot-3.x** 的项目, 必须满足 `java.version >= 17`

:::

## spring-boot

### 请先读我

::: danger

注意区分 `jt-808-server-spring-boot-starter` 和 `jt-808-server-spring-boot-starter-boot2` 两个 `starter`, 从 **2.1.x** 开始:

- `jt-808-server-spring-boot-starter` 是给 **spring-boot-3.x** 提供的
- `jt-808-server-spring-boot-starter-boot2` 是给 **spring-boot-2.x** 提供的

:::

### 内置版本

除了 `jt-808-server-spring-boot-starter` 模块依赖的 **spring-boot版本** 是 **3.3.0** 外，其他模块依赖的 **spring-boot版本** 都是 **2.7.18**。

这里只讨论 **spring-boot版本**，至于 **spring-boot** 依赖的 **spring-framework** 的版本以对应版本的 `spring-boot-dependencies` 依赖为准。

::: info 提示

你可以按你的需求调整 **jt-framework** 中默认的 **spring-boot** 版本。

:::

### spring-boot-2.x

在 **spring-boot-2.x** 的项目中，你可以像下面这样修改 `jt-framework` 自带的 **spring-boot** 版本:

```xml

<dependencies>
    <dependency>
        <groupId>io.github.hylexus.jt</groupId>
        <artifactId>jt-808-server-spring-boot-starter-boot2</artifactId>
        <version>2.3.0-rc.1</version>
        <!-- 1. 排除 jt-framework 自带的 spring-boot-starter(2.7.18) -->
        <exclusions>
            <exclusion>
                <artifactId>spring-boot-starter</artifactId>
                <groupId>org.springframework.boot</groupId>
            </exclusion>
        </exclusions>
    </dependency>
    <!-- 2. 按需求引入需要的 spring-boot 版本 -->
    <dependency>
        <artifactId>spring-boot-starter</artifactId>
        <groupId>org.springframework.boot</groupId>
        <!-- 使用2.5.12替换 jt-framework内置的 2.7.18     -->
        <!-- jt-808-server-spring-boot-starter-boot2(注意和 spring-boot-3.x项目的区别) 对应的 spring-boot.version ∈ [2.2.x, 2.6.x]     -->
        <!-- jt-808-server-spring-boot-starter-boot2(注意和 spring-boot-3.x项目的区别) 对应的 spring-boot.version ∈ [2.2.x, 2.6.x]     -->
        <!-- jt-808-server-spring-boot-starter-boot2(注意和 spring-boot-3.x项目的区别) 对应的 spring-boot.version ∈ [2.2.x, 2.6.x]     -->
        <version>2.5.12</version>
    </dependency>
</dependencies>
```

### spring-boot-3.x

在 **spring-boot-3.x** 的项目中，你可以像下面这样修改 `jt-framework` 自带的 **spring-boot** 版本:

```xml

<dependencies>

    <dependency>
        <groupId>io.github.hylexus.jt</groupId>
        <artifactId>jt-808-server-spring-boot-starter</artifactId>
        <version>2.3.0-rc.1</version>
        <exclusions>
            <!-- 1. 排除 jt-framework 自带的 spring-boot-starter(3.3.0) -->
            <exclusion>
                <artifactId>spring-boot-starter</artifactId>
                <groupId>org.springframework.boot</groupId>
            </exclusion>
        </exclusions>
    </dependency>
    <!-- 2. 按需求引入需要的 spring-boot 版本 -->
    <dependency>
        <artifactId>spring-boot-starter</artifactId>
        <groupId>org.springframework.boot</groupId>
        <!-- 使用3.0.0替换 jt-framework内置的 3.3.0     -->
        <!-- jt-808-server-spring-boot-starter(注意和 spring-boot-2.x项目的区别) 对应的 spring-boot.version ∈ [3.0.x, ...]     -->
        <!-- jt-808-server-spring-boot-starter(注意和 spring-boot-2.x项目的区别) 对应的 spring-boot.version ∈ [3.0.x, ...]     -->
        <!-- jt-808-server-spring-boot-starter(注意和 spring-boot-2.x项目的区别) 对应的 spring-boot.version ∈ [3.0.x, ...]     -->
        <version>3.0.0</version>
    </dependency>

</dependencies>
```
