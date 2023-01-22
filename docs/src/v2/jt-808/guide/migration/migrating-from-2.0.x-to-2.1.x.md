---
icon: merge
---

# 2.0.x升级到2.1.x

## 请先读我

::: danger

**2.1.x** 的变更如下:

- 1). 模块名称拼写错误修改
    - **2.0.x** 中的 `jt-808-server-spring-boot-stater` 有单词拼写错误(😂): **starter** 写成了 **stater**
    - 在 **2.1.x** 中改成了 `jt-808-server-spring-boot-starter-boot2`
        - **stater** 修改为 **starter**
        - 添加了 **-boot2** 后缀，表示这个模块是给 **spring-boot-2.x** 提供的
- 2). **JDK版本** 和 **spring-boot版本** 修改
    - `jt-808-server-spring-boot-starter`
        - `JDK`: **17**
        - `.class` 文件 : **JDK-17**
        - `spring-boot`: **3.0.2**
    - `jt-808-server-spring-boot-starter-boot2` 是从之前的 `jt-808-server-spring-boot-stater` 重命名的模块
        - `JDK`: **17**
        - `.class` 文件 : **JDK-11**
    - `spring-boot`: **2.6.14**
- 3). **spring-boot版本** 升级
    - 从 **2.5.12** 升级到 **2.6.24**
    - 新增了 **spring-boot-3.x** 的支持

:::

## 依赖升级

### spring-boot-2.x

使用 **spring-boot-2.x** 的项目:

- 将之前的 `jt-808-server-spring-boot-stater` 依赖改为 `jt-808-server-spring-boot-starter-boot2`
    - `stater --> starter`
    - 添加了 `-boot2` 后缀
- 版本改为 `2.1.x` 的最新版即可

**2.0.x** 的依赖如下:

::: code-tabs#dependency

@tab maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-stater</artifactId>
    <version>2.0.3-RELEASE</version>
</dependency>
```

@tab:active gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-stater', version: '2.0.2-RELEASE'
```

:::

修改为 **2.1.x** 的依赖坐标:

::: code-tabs#dependency

@tab maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter-boot2</artifactId>
    <version>2.1.0-rc1</version>
</dependency>
```

@tab:active gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-starter-boot2', version: '2.1.0-rc1'
```

:::

### spring-boot-3.x

使用 **spring-boot-3.x** 的项目:

- 将之前的 `jt-808-server-spring-boot-stater` 依赖改为 `jt-808-server-spring-boot-starter`
    - `stater --> starter`
- 版本改为 `2.1.x` 的最新版即可

**2.0.x** 的依赖如下:

::: code-tabs#dependency-boot3

@tab maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-stater</artifactId>
    <version>2.0.3-RELEASE</version>
</dependency>
```

@tab:active gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-stater', version: '2.0.2-RELEASE'
```

:::

修改为 **2.1.x** 的依赖坐标:

::: code-tabs#dependency-boot3

@tab maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter</artifactId>
    <version>2.1.0-rc1</version>
</dependency>
```

@tab:active gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-starter', version: '2.1.0-rc1'
```

:::
