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
    - `jt-808-server-spring-boot-starter-3.0.1.jar`
- **spring-boot-2.x**
    - `jt-808-server-spring-boot-starter-boot2-3.0.1.jar`

按需求引入为 `808协议` 提供的 `spring-boot-starter`：

- gradle

```groovy
implementation group: 'io.github.hylexus.jt', name: 'jt-808-server-spring-boot-starter-boot2', version: "3.0.1"
```

- maven

```xml

<dependency>
    <groupId>io.github.hylexus.jt</groupId>
    <artifactId>jt-808-server-spring-boot-starter-boot2</artifactId>
    <version>3.0.1</version>
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

Original Java backend code that the maintainer has authority to license uses [Apache-2.0](LICENSE).
Original documentation and site code use [MIT](docs/LICENSE); the Dashboard UI retains
[MIT](dashboard/jt-dashboard-server-ui-vue3/LICENSE), and sample frontend MIT notices are in their static directories.
Java documentation examples and original project JT artwork use Apache-2.0; see the
[documentation license scope](docs/README.md). Other contributions retain their existing licenses and express grants.

历史贡献的许可确认尚未完成。各项贡献按其原有许可及权利人的明确授权范围处理，本项目的许可证声明不扩大未确认贡献的授权范围，详见 [授权记录](docs/license-consents.md)。正式发行前应处理未覆盖的贡献，并确定新版本及最终许可范围；Maven POM 的许可名称与链接将在发行范围确认后配置。当前发行安排见 [许可发行清单](docs/license-release-checklist.md)。

Permissions for historical contributions have not all been confirmed; project notices do not expand the rights granted
for unconfirmed contributions. See the [consent records](docs/license-consents.md).
Before a formal release, address contributions outside the confirmed grants and determine the new version
and final license scope. Maven POM license names and URLs will be configured once that scope is confirmed;
see the [release checklist](docs/license-release-checklist.md).

第三方依赖和改编源码保留各自许可。Symphonia 改编文件保留 MPL-2.0，Checkstyle 衍生构建配置保留 LGPL-2.1-or-later。后端复制和改编源码的来源、版权、共用 MIT 条款及 Sun 声明集中维护在 [NOTICE](NOTICE)。

Third-party dependencies and adapted sources retain their own licenses. The Symphonia-derived file retains
MPL-2.0, and the Checkstyle-derived configuration retains LGPL-2.1-or-later. Source attributions,
copyrights, shared MIT terms and Sun notices for copied and adapted backend code are consolidated in [NOTICE](NOTICE).

1078 使用未修改的外部依赖 `de.sciss:jump3r:1.0.5`，上游 POM 声明 LGPL-2.1-or-later；其 FFT 文件头中的历史权利说明与该声明的适用关系尚未明确，原文和核查依据继续保存在 [1078 说明](docs/license-1078-review.md)。普通库不附带该外部库的源码或二进制；实际捆绑它的应用需提供适用声明、匹配源码及重建／替换材料，准备这些材料不代表未决事项已经解决。

The 1078 modules use the unmodified external dependency `de.sciss:jump3r:1.0.5`, whose upstream POM declares
LGPL-2.1-or-later. The relationship between the historical rights statements in its FFT source header and
that declaration remains unresolved; see the [1078 review](docs/license-1078-review.md).
Ordinary library JARs do not bundle jump3r binaries or sources. Applications bundling it need applicable notices, matching sources and rebuild/replacement
materials; preparing these materials does not resolve the outstanding rights questions.

中央仓库发布名单为 core/808 的 8 个模块，不包含 1078、Dashboard 和示例；名单内的模块没有依赖或捆绑 jump3r。

The Maven Central publication list contains eight core/808 modules, excluding 1078, Dashboard and samples.
The listed modules neither depend on nor bundle jump3r.

## 依赖许可与构建

普通 Java 库的 JAR、源码 JAR 和 Javadoc JAR 统一附带 `META-INF/LICENSE.txt` 和 `META-INF/NOTICE`；复制源码的版权及条款集中在根 NOTICE。`jt-1078-server-support` 还附带 MPL 全文和该许可覆盖文件的匹配源码。依赖报告和应用分发的补充材料按需生成。

需要查看依赖时按需生成报告：

```shell
./gradlew :generateLicenseReport
# 或指定模块
./gradlew :jt-1078-server-support:generateLicenseReport
```

报告输出到相应 `build/reports/dependency-license/`，包含 HTML、JSON 和文本声明。完整应用的配套许可／源码材料也按需生成：

```shell
./gradlew :samples:jt-1078-server-sample-webflux-boot3:prepareDistributionLicenses \
  -Pjt-framework.backend.build.debug-module-fatjar.enabled=true
```

任务为实际 Boot JAR 准备额外需要的源码包和发行声明，输出到 `build/reports/distribution-licenses/` 和同级 ZIP，核对后随应用交付。普通依赖的原始声明保留在嵌套 JAR 中。详情见 [后端分发说明](docs/license-maintenance.md#后端)。

Dashboard 和文档站点分别在各自的 `public/NOTICE.txt` 中集中保留第三方署名和适用条款，由 Vite／VuePress 复制到产物。依赖或静态资源的来源、版权或许可变化时更新对应声明，具体路径和构建命令见 [许可维护](docs/license-maintenance.md)。

日常维护以依赖及锁文件、原始声明和实际交付内容为准；历史调查可从 Git 历史追溯。

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
