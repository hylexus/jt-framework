# jt-framework

<p align="center">
    <a href="https://github.com/hylexus/jt-framework">
        <img alt="jt-framework logo" src="docs/src/.vuepress/public/logo.png" width="160" height="160" />
    </a>
</p>

<p align="center">
    <a href="https://deepwiki.com/hylexus/jt-framework">
        <img src="https://deepwiki.com/badge.svg" alt="Ask DeepWiki">
    </a>
    <br/>
    <a href="https://openjdk.org/projects/jdk/17">
        <img alt="JDK" src="https://img.shields.io/badge/JDK-8/17/21-red" />
    </a>
    <a href="https://central.sonatype.com/namespace/io.github.hylexus.jt">
        <img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.hylexus.jt/jt-core.svg?label=Maven%20Central&color=green-light" />
    </a>
    <br/>
    <a href="https://github.com/hylexus/jt-framework/actions/workflows/gradle-build-linux-platform.yml">
        <img alt="Gradle Build" src="https://img.shields.io/github/actions/workflow/status/hylexus/jt-framework/gradle-build-linux-platform.yml?logo=gradle&label=Gradle%20Build">
    </a>
    <a href="https://github.com/hylexus/jt-framework/graphs/commit-activity">
        <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/hylexus/jt-framework?logo=github&label=Last%20Commit&color=blue">
    </a>
    <br/>
    <a href="https://github.com/hylexus/jt-framework/graphs/commit-activity">
        <img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/t/hylexus/jt-framework?logo=github&label=Total%20Commits"/>
    </a>
    <a href="https://github.com/hylexus/jt-framework/graphs/commit-activity">
        <img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/w/hylexus/jt-framework?logo=github&label=Commit%20Activity"/>
    </a>
    <a href="https://github.com/hylexus/jt-framework/graphs/commit-activity">
        <img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/hylexus/jt-framework?logo=github&label=Commit%20Activity"/>
    </a>
    <a href="https://github.com/hylexus/jt-framework/graphs/commit-activity">
        <img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/y/hylexus/jt-framework?logo=github&label=Commit%20Activity"/>
    </a>
</p>

## Tips / 提示

> 2026.1 版的 idea 中打开项目可能会报错

- 临时解决方案: 临时禁用掉 idea 内置的 `Spark` 插件
- 详情参考: [https://youtrack.jetbrains.com/issue/IDEA-386409](https://youtrack.jetbrains.com/issue/IDEA-386409)

## Intro / 介绍

Jt-808协议服务端。

- 最新版依赖请查看: [Maven 中央仓库地址](https://central.sonatype.com/namespace/io.github.hylexus.jt)
- 2.x 版升级问题，请参考 [升级指南--2.0.x升级到2.1.x](https://hylexus.github.io/jt-framework/v2/jt-808/guide/migration/migrating-from-2.0.x-to-2.1.x.html)

## 私有协议编解码库推荐

不依赖具体协议的私有协议编解码库。

- Github : https://github.com/hylexus/xtream-codec
- Gitee : https://gitee.com/hylexus/xtream-codec

> 从 **3.0.0-rc.1** 开始，**jt-framework** 内置了 [xtream-codec-core](https://hylexus.github.io/jt-framework/v2/jt-808/guide/xtream-codec-adapter/intro.html) 的适配器。

## Compatibility / 兼容性

支持 **spring-boot-2.x** [![spring-boot-2.x](https://img.shields.io/maven-central/v/io.github.hylexus.jt/jt-808-server-spring-boot-starter-boot2.svg?label=spring-boot-2.x)](https://search.maven.org/search?q=g:%22io.github.hylexus.jt%22%20AND%20a:%22jt-808-server-spring-boot-starter-boot2%22)
和 **spring-boot-3.x** [![spring-boot-3.x](https://img.shields.io/maven-central/v/io.github.hylexus.jt/jt-808-server-spring-boot-starter.svg?label=spring-boot-3.x)](https://search.maven.org/search?q=g:%22io.github.hylexus.jt%22%20AND%20a:%22jt-808-server-spring-boot-starter%22) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

更多有关版本兼容性的信息，请移步: [入门--兼容性](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/compatibility.html) 。

`starter` 的 **JDK** 版本、**spring-boot** 版本限制如下：

| Module                                     | JDK   | spring-boot       | Desc                                       |
|--------------------------------------------|-------|-------------------|--------------------------------------------|
| `jt-808-server-spring-boot-starter-boot2`  | `8+`  | `[2.2.x, 2.7.18]` | 为 **spring-boot-2.x** 提供的 starter          |
| `jt-808-server-spring-boot-starter`        | `17+` | `[3.0.0, ...]`    | 为 **spring-boot-3.x** 提供的 starter          |
| `jt-1078-server-spring-boot-starter-boot2` | `8+`  | `[2.2.x, 2.7.18]` | 为 **spring-boot-2.x** 提供的 starter; `beta版` |
| `jt-1078-server-spring-boot-starter`       | `17+` | `[3.0.0, ...]`    | 为 **spring-boot-3.x** 提供的 starter; `beta版` |
| `jt-dashboard-client-spring-boot-starter`  | `17+` | `[3.0.0, ...]`    | 暂时只支持 `jdk17+/spring-boot-3.x`; `beta版`    |
| `jt-dashboard-server-spring-boot-starter`  | `17+` | `[3.0.0, ...]`    | 暂时只支持 `jdk17+/spring-boot-3.x`; `beta版`    |

## Modules / 项目模块

本项目默认的 **JDK版本** 和 **spring-boot版本** 见下表(有必要的话,根据实际情况自行调整):

| Module                                   | JDK | CompileLevel | .class      | spring-boot | Desc                                      |
|------------------------------------------|-----|--------------|-------------|-------------|-------------------------------------------|
| jt-core                                  | 17  | JDK-8        | 52 (JDK-8)  | --          |                                           |
| jt-808-server-spring-boot-starter        | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.3.0**_ |                                           |
| jt-808-server-spring-boot-starter-boot2  | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-808-server-spring-boot-autoconfigure  | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-808-server-support                    | 17  | JDK-8        | 52 (JDK-8)  | --          |                                           |
| jt-1078-server-spring-boot-starter       | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.3.0**_ |                                           |
| jt-1078-server-spring-boot-starter-boot2 | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-1078-server-spring-boot-autoconfigure | 17  | JDK-8        | 52 (JDK-8)  | 2.7.18      |                                           |
| jt-1078-server-support                   | 17  | JDK-8        | 52 (JDK-8)  | --          |                                           |
| `dashboard/**`                           | 17  | _**JDK-17**_ | 61 (JDK-17) | _**3.3.0**_ | dashboard 模块暂时只支持 `spring-boot-3.x/jdk17` |

- 模块介绍

```sh
.
├── build-script   # gradle 用到的构建脚本和checkstyle配置
├── docs  # 文档 (vue-press)
├── jt-1078-server-spring-boot-autoconfigure  # (jdk8+)
├── jt-1078-server-spring-boot-starter        # spring-boot-v3 自动配置 (jdk17+)
├── jt-1078-server-spring-boot-starter-boot2  # spring-boot-v2 自动配置 (jdk8+)
├── jt-1078-server-support                    # jt-1078 的实现 (jdk8+)
├── jt-808-server-spring-boot-autoconfigure   # (jdk8+)
├── jt-808-server-spring-boot-starter         # spring-boot-v3 自动配置 (jdk17+)
├── jt-808-server-spring-boot-starter-boot2   # spring-boot-v2 自动配置 (jdk8+)
├── jt-808-server-support                     # jt-808 的实现 (jdk8+)
├── jt-core                                   # 一些公共类 (jdk8+)
├── dashboard # dashboard 模块 「暂时」 不支持 spring-boot-2.x
│     ├── jt-dashboard-client                       # dashboard 客户端 (jdk17+)
│     ├── jt-dashboard-client-spring-boot-starter   # dashboard 客户端的 spring-boot-v3 自动配置 (jdk17+)
│     ├── jt-dashboard-common                       # dashboard 公共依赖 (jdk17+)
│     ├── jt-dashboard-server                       # dashboard 服务端 (jdk17+)
│     ├── jt-dashboard-server-spring-boot-starter   # dashboard 服务端的 spring-boot-v3 自动配置 (jdk17+)
│     └── jt-dashboard-server-ui-vue3               # dashboard 的 UI(基于 VUE3)
├── demos # (jdk17+)
│     ├── jt-demo-1078-server-webflux-boot3
│     ├── jt-demo-808-server-webflux-boot3
│     ├── jt-demo-dashboard-webflux-boot3
│     └── jt-demo-dashboard-webmvc-boot3
└── samples
      ├── jt-1078-server-sample-webflux-boot3 # JT/T 1078 的示例(spring-boot-3.x; Webflux) (jdk17+)
      ├── jt-1078-server-sample-webmvc-boot3  # JT/T 1078 的示例(spring-boot-3.x; SpringMVC) (jdk17+)
      ├── jt-808-server-sample-annotation     # 注解相关的示例(spring-boot-2.x) (jdk8+)
      ├── jt-808-server-sample-bare           # 几乎零配置的示例(spring-boot-2.x) (jdk8+)
      ├── jt-808-server-sample-bare-boot3     # 几乎零配置的示例(spring-boot-3.x) (jdk17+)
      ├── jt-808-server-sample-customized     # 定制化示例(spring-boot-2.x) (jdk8+)
      ├── jt-808-client-sample-debug          # 开发时用来Debug的项目,请忽略
      ├── jt-808-server-sample-debug          # 开发时用来Debug的项目,请忽略
      └── jt-sample-common                    # samples 项目共同的依赖 (jdk8+)

```

## Docs / 文档

- Github：[https://hylexus.github.io/jt-framework/](https://hylexus.github.io/jt-framework/)
- DeepWiki(**AI** 生成): https://deepwiki.com/hylexus/jt-framework/

## QuickStart / 快速入门

更多信息，请移步 [入门--快速开始](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/quick-start.html) 。

### 1. 创建工程

创建一个空的 `spring-boot` 工程。

### 2. 添加依赖

- **spring-boot-3.x**
    - `jt-808-server-spring-boot-starter-3.0.0.jar`
- **spring-boot-2.x**
    - `jt-808-server-spring-boot-starter-boot2-3.0.0.jar`

按需求引入为 `808协议` 提供的 `spring-boot-starter`：

- gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-starter-boot2', version: "3.0.0"
```

- maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter-boot2</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 3. 配置

- application.yml

```yaml
jt808:
  built-components:
    component-statistics:
      enabled: true
    request-handlers:
      enabled: true
logging:
  level.root: info
  level.io.github.hylexus: info
  level.jt-808.request.decoder: debug
  level.jt-808.response.encoder: debug
```

## Features / 特性

- 注解驱动开发
- 支持苏标附件服务器
- 支持消息下发
- 支持消息分包
- 支持多版本
- 支持报文加解密

## Maven Samples / Maven 版示例

Maven 版示例项目

- Github: [https://github.com/hylexus/jt-framework-samples-maven](https://github.com/hylexus/jt-framework-samples-maven)
- Gitee: [https://gitee.com/hylexus/jt-framework-samples-maven](https://gitee.com/hylexus/jt-framework-samples-maven)

## Building from Source / 构建源码

更多有关编译源码的信息，请移步 [入门--编译源码](https://hylexus.github.io/jt-framework/v2/jt-808/guide/quick-start/building-from-source.html) 。

- Mac/Linux/Unix-Based System

```shell script
./gradlew clean build
```

- Windows

```shell script
.\gradlew.bat clean build
```

## License / 许可证

维护者有权授权的原创 Java 后端代码采用 [Apache License 2.0](LICENSE)。原创文档和文档站点代码采用 [MIT](docs/LICENSE)，Dashboard UI 保持 [MIT](dashboard/jt-dashboard-server-ui-vue3/LICENSE)，示例前端的 MIT 声明位于各自静态目录。文档中的 Java 示例和本项目自行绘制的 JT 标识及其衍生图形采用 Apache-2.0，具体范围见 [文档许可说明](docs/README.md)。其他贡献者的内容按其原有许可及明确确认的范围处理。

历史贡献的许可确认尚未完成。各项贡献按其原有许可及权利人的明确授权范围处理，本项目的许可证声明不扩大未确认贡献的授权范围，详见 [授权记录](docs/license-consents.md)。正式发行前应处理未覆盖的贡献，并确定新版本及最终许可范围；Maven POM 的许可名称与链接将在发行范围确认后配置。当前发行安排见 [许可发行清单](docs/license-release-checklist.md)。

第三方依赖和改编源码保留各自许可。Symphonia 改编的 IMA ADPCM 文件保留 MPL-2.0，改自 Checkstyle 的构建配置保留 LGPL-2.1-or-later，H.264 中来自 jmuxer 的代码保留 MIT，Sun G.7xx 文件保留原始声明。来源、适用文件及修改说明见 [第三方源码声明](THIRD-PARTY-SOURCE-NOTICES.txt)，前端依赖见 [前端第三方声明](THIRD-PARTY-FRONTEND-NOTICES.txt)。

1078 使用未修改的外部依赖 `de.sciss:jump3r:1.0.5`。其上游 POM 声明许可为 LGPL-2.1-or-later；本项目的 Apache-2.0 许可不适用于该依赖。

该版本中的 `de/sciss/jump3r/mp3/FFT.java` 保留了涉及 GPL 来源及其他历史权利事项的文件头说明，原文见 [上游声明摘录](licenses/jump3r/UPSTREAM-NOTICES.txt)。现有核查材料尚未明确该历史说明与上游 LGPL 声明对该文件的具体适用关系；核查依据及未确定事项见 [1078 与前端许可说明](docs/license-1078-review.md)。

本项目已依据上游 LGPL 声明准备许可、源码及重建／替换材料。Git 源码仓库通过 Gradle 声明依赖；构建生成的相关普通 JAR 已附带 jump3r 源码归档，交付这些 JAR 同样涉及库源码的分发。发行包含其源码或二进制的产物前，应针对精确版本确认许可依据；包含库二进制的应用还需提供匹配源码和可用的重建／替换方法。

当前中央仓库发布名单包含 core/808 的 8 个模块；这些模块没有依赖或捆绑 jump3r。1078、Dashboard 和示例暂不加入中央仓库发布名单，其源码和分发材料仍按相应许可证处理。

## Dependency Licenses / 依赖许可证报告

项目集成了 [Gradle License Report](https://github.com/jk1/Gradle-License-Report) 插件，可读取依赖的 Maven POM、JAR Manifest 和许可证文件，生成直接及传递依赖的许可证报告。

- 生成所有 Java 模块的汇总报告（包括 `dashboard`、`samples` 和 `demos`）：

```shell
./gradlew :generateLicenseReport
```

- 仅生成指定模块的报告，例如包含 MP3 编码依赖 `de.sciss:jump3r` 的 1078 模块：

```shell
./gradlew :jt-1078-server-support:generateLicenseReport
```

汇总报告位于根目录的 `build/reports/dependency-license/`，模块报告位于对应模块的 `build/reports/dependency-license/`：

- `index.html`：按许可证分组的报告，未识别到许可证的依赖列入 `Unknown`。
- `index.json`：保留每个依赖的全部已发现许可证，便于后续人工核对或自动处理。
- `THIRD-PARTY-NOTICES.txt`：文本报告，包含依赖坐标、许可证声明和链接，以及插件找到的依赖归档内许可证文件正文。

每个 Java 模块的普通 JAR、源码 JAR、Javadoc JAR，以及启用打包的 Spring Boot JAR，都会在生成前自动执行该模块的报告任务，并包含以下内容：

- `META-INF/LICENSE.txt`、`META-INF/NOTICE`：项目许可与声明。
- `META-INF/THIRD-PARTY-NOTICES.txt`：所属模块的 JVM 依赖报告。
- `META-INF/THIRD-PARTY-SOURCE-NOTICES.txt`、`META-INF/THIRD-PARTY-FRONTEND-NOTICES.txt`：仓库级源码与前端声明。
- `META-INF/licenses/`：额外的 MPL、LGPL、MIT、Sun 等许可材料。
- 包含 jump3r 运行时依赖时，`META-INF/third-party-sources/jump3r/` 提供匹配版本的 Maven 源码 JAR 和重新构建说明；包含 1078 support 模块时，`META-INF/third-party-sources/symphonia/` 提供 MPL 覆盖的 Java 源码。

每个 JAR 使用所属模块的依赖报告；根项目报告用于汇总查看。仓库级声明可能提及当前模块未使用的源码或前端库。第三方源码准备任务随 JAR 自动执行，匹配的 jump3r 源码不可获取时会使打包失败。

`licenses/source-audit.json` 和两份前端 `*-inventory.json` 供仓库内核对、生成与检查使用，不随 JAR 分发；原始声明、来源记录及必要的许可／源码材料继续附带。

完整应用的额外许可材料按需准备，不参与普通构建：

```shell
./gradlew :samples:jt-1078-server-sample-webflux-boot3:prepareDistributionLicenses \
  -Pjt-framework.backend.build.debug-module-fatjar.enabled=true
```

任务按实际 Boot JAR 生成配套许可/源码目录和 ZIP，位于该模块的
`build/reports/distribution-licenses/` 及 `distribution-licenses.zip`。
下载缓存和生成材料均留在 `build/`，仓库不维护全量 JVM 文本副本或
固定依赖哈希清单。交付完整应用时审核并随应用一起提供配套材料；
详见 [JVM 许可核对与发行材料](docs/license-jvm-review.md)。
普通 `jar`、源码/Javadoc JAR、`bootJar` 和 `build` 不自动运行此任务。

扫描范围为 `compileClasspath`、`runtimeClasspath` 和 `annotationProcessor`，包括 `compileOnly` 依赖；不包含测试专用依赖、Gradle 构建插件或前端 npm 依赖。同组的项目模块及无实际 JAR 的 BOM 不列为第三方依赖。报告可单独生成，JAR 打包也会自动生成所需报告。

首次生成报告需要联网获取依赖及其 POM 元数据；离线运行可能缺少许可证信息。需要重新生成时可添加 `--rerun-tasks`。优先读取依赖自己的 POM 许可证声明，仅在未声明时继承父 POM 的许可证。

报告收集的是依赖声明和归档内的许可信息，不能自动判断项目是否可以采用 Apache 2.0，也不会扫描已复制到项目中的第三方源码。未知许可证、多许可证的具体含义及发布义务仍需核实，JAR 中的第三方声明也可能带来额外许可证条目。`net.minecraftforge.licenser` 用于检查和维护源码许可证头，不提供依赖许可证报告功能。

如果依赖归档中没有许可证文件，文本报告只能包含其 POM 或 Manifest 中声明的名称和链接。例如 `de.sciss:jump3r:1.0.5` 会列出 `LGPL v2.1+` 及其链接，插件不会自动下载全文；本项目单独补充了 LGPL 文本和源码材料。自动报告中 `net.jcip:jcip-annotations:1.0` 的 Unknown 已在人工源码声明中注明为其源码头声明的 CC-BY-2.5。

前端声明单独维护，更新锁文件后先核对 `licenses/frontend-inventory.json`，再运行 `python3 build-script/generate-frontend-license-notices.py`。该脚本从哈希匹配的 npm 归档提取声明，Gradle 打包使用已生成的文本，无需在每次构建时访问 npm。

后续升级依赖、调整第三方源码或静态资源时，按[依赖与许可维护清单](docs/license-maintenance.md)执行对应流程，其中列明人工更新项、生成命令、检查方式及提交范围。

## Reporting Issues / 提交问题

- **GitHub** 提交 **Issue**
- **QQ**交流群 : **1072477383**

## Community / 社区

|                                QQ交流群-1群                                |                                QQ交流群-2群                                |
|:----------------------------------------------------------------------:|:----------------------------------------------------------------------:|
|                      ~~1072477383~~ （1群已满, 请加2群)                       |                               1082632499                               |
| ![QQ交流群-1群](https://hylexus.github.io/jt-framework/img/QQ-Group-1.png) | ![QQ交流群-2群](https://hylexus.github.io/jt-framework/img/QQ-Group-2.png) |

## Funding / 打赏

项目的发展离不开你的支持，请作者喝一杯🍺吧！

![有钱的捧个钱场 没钱的捧个人场](https://hylexus.github.io/jt-framework/img/pay.png)
