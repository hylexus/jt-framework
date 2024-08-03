---
icon: code
---

# 编译源码

## 请先读我

::: info 提示

一般情况下，你无需编译源码。直接引 为 `spring-boot` 提供的 `starter` 即可：

- `spring-boot-3.x` : 引入 `jt-808-server-spring-boot-starter`
- `spring-boot-2.x` : 引入 `jt-808-server-spring-boot-starter-boot2`

:::

如果你要体验最新版 或者 要基于源码二次开发，可以参考下文的说明来编译源码：

## 要求

### 为什么编译源码一定要 JDK17？

::: tip 为什么编译源码一定要 JDK17？

因为 `jt-808-server-spring-boot-starter` 模块是为 `spring-boot-3.x` 提供的，`spring-boot-3.x` 必须使用 `JDK17+`。

其他模块虽然也用了 `JDK17`，但是编译级别是 `JDK8`。

:::

### JDK版本要求

从 `2.1.0` 开始支持 `spring-boot-3.x`，所以编译源码的 `JDK` 版本必须在 `JDK-17` (包括)以上。

::: info

虽然源码要求的 `JDK` 版本必须在 `17` 以上，但是实际上：

- 只有 `jt-808-server-spring-boot-starter` 模块必须使用 `JDK-17`，编译后的 `.class` 文件版本为 `61` (`JDK-17`)
- 其余模块编译后的 `.class` 文件版本依然是 `52` (`JDK-11`)；也就是说使用 `JDK17` 编译输出了 `JDK8` 对应的 `.class`

:::

各个模块的 **JDK版本** 见下面表格：

| Module                                  | JDK | CompileLevel | .class      |
|-----------------------------------------|-----|--------------|-------------|
| jt-808-server-spring-boot-starter       | 17  | _**JDK-17**_ | 61 (JDK-17) |
| jt-808-server-spring-boot-starter-boot2 | 17  | JDK-8        | 52 (JDK-8)  |
| jt-808-server-spring-boot-autoconfigure | 17  | JDK-8        | 52 (JDK-8)  |
| jt-808-server-support                   | 17  | JDK-8        | 52 (JDK-8)  |
| jt-808-server-core                      | 17  | JDK-8        | 52 (JDK-8)  |

在 `gradle.properties` 配置文件里有两个 `JDK` 版本的配置:

- `maximumJavaVersion` 是 `jt-808-server-spring-boot-starter` 用到的 **JDK版本**
- `defaultJavaVersion` 是其余模块用到的

```properties
# spring-boot-2.x
defaultJavaVersion=8
# spring-boot-3.x
maximumJavaVersion=17
```

::: warning

- 如果没有特殊需求，不建议对 `defaultJavaVersion` 降级(改为比 **8** 更低的版本)
    - 因为当前源码的最低要求是 `JDK8`
    - 降级之后可能有一些 `Java` 语法不兼容
- `maximumJavaVersion` 这个配置项必须 `>= 17`, 因为这个是给 `spring-boot-3.x` 用的
- 但是你可以将 `defaultJavaVersion` 或 / 和 `maximumJavaVersion` 升级(改为高版本)

:::

### spring-boot版本要求

在 `gradle.properties` 配置文件里有两个 `spring-boot` 版本的配置:

- `maximumSpringBootBomVersion` 是 `jt-808-server-spring-boot-starter` 用到的 `spring-boot` 版本
- `defaultSpringBootBomVersion` 是其余模块用到的 `spring-boot` 版本

```properties
# spring-boot-2.x
defaultSpringBootBomVersion=2.7.18
# spring-boot-3.x
maximumSpringBootBomVersion=3.3.0
```

::: info

如果没有特殊需求，也没必要修改源码中的 `spring-boot` 版本。

- `defaultSpringBootBomVersion` 可取值为 `[2.2.x, 2.7.18]`
- `maximumSpringBootBomVersion` 可取值为 `[3.0.0, ...)`

:::

## 编译

::: warning

不建议自己单独安装 `gradle`。你应该直接使用项目中指定版本的 `gradle-wrapper`。

:::

### 命令行编译

编译命令：

```shell
./gradlew clean build
```

::: details

```shell
~ ls
CHANGELOG.md                            gradle.properties                       jt-808-server-support
README.md                               gradlew                                 jt-core
build-script                            gradlew.bat                             jt-spring
build.gradle                            jt-808-server-spring-boot-autoconfigure samples
docs                                    jt-808-server-spring-boot-starter       settings.gradle
gradle                                  jt-808-server-spring-boot-starter-boot2

# 使用 项目中自带的 gradlew 来编译
~ ./gradlew clean build

> Configure project :
the maven repo credentials file <<repo-credentials.gradle>> will be load from : /xxx/xxx/.gradle/repo-credentials.gradle
[[ MavenRepo ]] --> (aliyun)[release] <-- [jt-808-server-spring-boot-autoconfigure]
[[ MavenRepo ]] --> (aliyun)[release] <-- [jt-808-server-spring-boot-starter]
[[ MavenRepo ]] --> (aliyun)[release] <-- [jt-808-server-spring-boot-starter-boot2]
[[ MavenRepo ]] --> (aliyun)[release] <-- [jt-808-server-support]
[[ MavenRepo ]] --> (aliyun)[release] <-- [jt-core]
[[ MavenRepo ]] --> (aliyun)[release] <-- [jt-spring]

BUILD SUCCESSFUL in 7s
73 actionable tasks: 71 executed, 2 up-to-date
```

:::

### 导入Idea

::: warning

- 请先给 **Idea** 装好 `lombok` 插件
- 请确保 `jdk.version >= 17`

:::

- 1). 项目 **JDK版本** 配置：

<p class="">
    <img :src="$withBase('/img/v2/quick-start/build-from-source/idea-project-structure.png')">
</p>

- 2). `Gradle` `JDK版本` 配置:

打开 `idea` 的 `gradle` 配置界面:

<p class="">
    <img :src="$withBase('/img/v2/quick-start/build-from-source/gradle-jdk-step-1.png')">
</p>

修改 `gradle` 用到的 **JDK版本** 为 **JDK17+**

<p class="">
    <img :src="$withBase('/img/v2/quick-start/build-from-source/gradle-jdk-step-2.png')">
</p>

### 导入Eclipse

::: warning

- 请先给 **Eclipse** 装好 `lombok` 插件
- 请确保 `jdk.version >= 17`

:::

::: danger 第一次导入报错

- 先执行 `./gradlew eclipse` 命令，为 `eclipse` 生成配置。
- 然后 `refresh` 所有模块

:::

导入后，效果如下：

<p class="">
    <img :src="$withBase('/img/v2/quick-start/build-from-source/eclipse-project-view.png')">
</p>


