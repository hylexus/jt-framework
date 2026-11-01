# 第三方依赖许可与分发

说明更新日期：2026-11-01；依赖核对范围沿用下列记录。
本文件记录 JVM 人工核对和依赖扫描的结论，保留固定来源、实际使用方式
和分发步骤。历史贡献授权见
[确认记录](license-consents.md)，当前发行范围见[发行清单](license-release-checklist.md)。

## 核对范围与结论

在已检查的版本、配置和分发方式下，除 jump3r 的未决许可事项外，
已核对依赖可按后文安排使用和分发，项目有权授权的原创后端代码采用
Apache-2.0。第三方组件保留各自许可，分发时仍需提供适用的声明、
许可文本和源码等材料。

jump3r 中 FFT 的历史权利说明与上游 LGPL 声明的具体适用关系尚未明确，
原始材料及核查依据见[1078 说明](license-1078-review.md)。
上述结论仅适用于下列核对范围。

| 范围                | 已检查材料                                                                                                         |
|---------------------|--------------------------------------------------------------------------------------------------------------------|
| JVM                 | 全部 Gradle 子项目及 buildSrc，335 个外部模块版本、344 个构件；含编译、运行、注解处理、测试、Checkstyle 和构建插件 |
| JVM 原文            | POM／父 POM、二进制 LICENSE／NOTICE、309 个匹配版本缓存源码归档；命中许可标记后结合上下文核对                      |
| Dashboard／文档前端 | 两个锁文件分别有 578／324 个包版本，合并去重为 837 个；55 个未安装的平台包另取完整性匹配的发布归档，均声明 MIT     |
| 前端分发资源        | Dashboard／播放器 67 个包及 3 组复制来源；文档独立清单 29 个包，最近生产构建实际记录 28 个，另覆盖搜索 Worker      |

根插件报告的 208 个条目和实际主代码配置中的 207 个外部二进制模块
并不相同：`hibernate-validator:8.0.0.Final` 只出现在报告中。按实际
解析内容判断，不能把报告中的全部条目都当成随 JAR 捆绑的库。

buildSrc 没有启用 Native target；自动创建的 Kotlin 2.0.21 Native
commonizer／prebuilt 配置未在离线缓存中解析，不计入已检查范围。
启用或分发 Native 工具链时另查其实际归档。未列入锁文件的安装目录
残留不计入上述锁定版本统计。

## 重点组件及处理

本表列出需要核对许可分支、文件级声明或分发条件的组件，并非 Apache-2.0
不兼容依赖清单。按当前独立依赖库的使用方式，Logback、Jakarta Annotations
保留各自的 EPL 许可，项目有权授权的自有代码采用 Apache-2.0。
复制或改编第三方实现、合并打包等使用方式变化时，应重新核对适用条款。

| 组件／版本                                                           | 原始声明与当前使用                                                                      | 分发安排                                                                                             |
|----------------------------------------------------------------------|-----------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| Logback classic/core 1.2.12、1.5.6                                   | EPL-1.0 或 LGPL-2.1；有运行时使用                                                       | 完整应用材料选择 EPL-1.0，保留原始双许可声明、全文和匹配源码                                         |
| Jakarta Annotations 1.3.5、2.1.1                                     | EPL-2.0，附带有条件的 GPL + Classpath exception 次级许可说明                            | 采用 EPL-2.0，保留 LICENSE.md／NOTICE.md 和匹配源码                                                  |
| Tomcat embed core 9.0.83、10.1.24                                    | 主体 Apache-2.0；含另按 CDDL-1.0／EPL-2.0 提供的 Schema                                 | 保留原始声明，完整应用材料提供对应可编辑 XSD／DTD                                                    |
| FindBugs annotations 3.0.1                                           | LGPL-2.1-or-later；808／1078 support 为 compileOnly，未进入已检查的生产运行时或应用 JAR | 当前不分发该库副本；改为运行时、shade 或复制源码后重新处理。注解引用不等于整个库被复制               |
| FindBugs jsr305 3.0.2                                                | 主体 Apache-2.0；四个并发注解另有 Brian Goetz 的 CC-BY-2.5 头，部分生产运行时使用       | 实际捆绑时从匹配源码提取 GuardedBy、Immutable、NotThreadSafe、ThreadSafe 原始头，并附 CC-BY-2.5 全文 |
| JCIP annotations 1.0                                                 | 四个源码头明确 CC-BY-2.5，Brian Goetz／Tim Peierls；POM 缺元数据，当前仅编译使用        | 自动 Unknown 不代表无许可；实际分发时保留署名、全文和匹配源码，与 jsr305 分别处理                    |
| JUnit Jupiter 5.8.2／5.10.2、Platform 1.8.2／1.10.2                  | EPL-2.0；仅测试配置                                                                     | 不进入生产运行时；另行分发测试环境时提供其材料                                                       |
| Checkstyle 9.3、Saxon-HE 10.6、trove4j 1.0.20200330                  | 分别为 LGPL-2.1+、MPL-2.0、LGPL-2.1+；构建／检查工具                                    | 运行工具不改变产品代码许可；分发完整工具链时另查材料。改编的 Checkstyle XML 另见源码说明             |
| Javassist 3.28.0-GA、JNA 5.7.0／5.13.0                               | 构建工具依赖，明确有 Apache-2.0 可选分支                                                | 可选择 Apache 分支，不将其他可选许可同时累加                                                         |
| jackson-core 2.14.2                                                  | 构建插件使用；三个 Schubfach 文件有 GPL-2.0 + Classpath exception 头                    | 保留文件级声明；已检查的产品运行时不包含该版本                                                       |
| Bouncy Castle 1.78.1、HdrHistogram 2.1.12／2.2.1、LatencyUtils 2.0.3 | 分别为 MIT 风格条款、CC0 及部分 BSD-2-Clause、CC0                                       | 保留各自全文和归档声明                                                                               |
| Fraction.js 4.3.7、SnakeYAML 1.30／2.2 的 Base64Coder                | 出现 GPL 字样，但明确有 MIT／Apache 等可选分支                                          | 采用对应允许的宽松许可分支                                                                           |
| xtream-codec-core 0.5.0-rc.1                                         | 主 LICENSE 为 Apache-2.0；聚合 NOTICE 包含其他组件的 EPL 等声明                         | 保留聚合声明，以本项目实际解析版本核对依赖，不将整个组件改判为 EPL                                   |

前端锁定包未发现仅有 GPL／AGPL、非商业或禁止再分发声明的组件。
除常见宽松许可外，还包括 Python-2.0、CC-BY-4.0、CC-BY-3.0 等，
仍按实际分发内容保留相应署名和条款。工具 README 中的示例或单独
图片的许可不自动代表整个代码包的许可。

## 固定版本的原始材料

Logback 原始许可见 [1.2.12](https://github.com/qos-ch/logback/blob/v_1.2.12/LICENSE.txt)
及 [1.5.6](https://github.com/qos-ch/logback/blob/v_1.5.6/LICENSE.txt)；
FindBugs 文件头见 [3.0.1 SuppressFBWarnings](https://github.com/findbugsproject/findbugs/blob/3.0.1/findbugs/src/java/edu/umd/cs/findbugs/annotations/SuppressFBWarnings.java)。
Jakarta 的 LICENSE.md／NOTICE.md、Tomcat 的 LICENSE／NOTICE 及
Schema／DTD 均以对应 Maven 版本归档内原文为依据。固定源码下载入口如下：

| 坐标                                                    | 原始材料                                                                                                                                        |
|---------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `ch.qos.logback:logback-classic:1.2.12`                 | [匹配源码](https://repo.maven.apache.org/maven2/ch/qos/logback/logback-classic/1.2.12/logback-classic-1.2.12-sources.jar)                       |
| `ch.qos.logback:logback-classic:1.5.6`                  | [匹配源码](https://repo.maven.apache.org/maven2/ch/qos/logback/logback-classic/1.5.6/logback-classic-1.5.6-sources.jar)                         |
| `ch.qos.logback:logback-core:1.2.12`                    | [匹配源码](https://repo.maven.apache.org/maven2/ch/qos/logback/logback-core/1.2.12/logback-core-1.2.12-sources.jar)                             |
| `ch.qos.logback:logback-core:1.5.6`                     | [匹配源码](https://repo.maven.apache.org/maven2/ch/qos/logback/logback-core/1.5.6/logback-core-1.5.6-sources.jar)                               |
| `com.google.code.findbugs:annotations:3.0.1`            | [匹配源码](https://repo.maven.apache.org/maven2/com/google/code/findbugs/annotations/3.0.1/annotations-3.0.1-sources.jar)                       |
| `io.github.hylexus.xtream:xtream-codec-core:0.5.0-rc.1` | [匹配源码](https://repo.maven.apache.org/maven2/io/github/hylexus/xtream/xtream-codec-core/0.5.0-rc.1/xtream-codec-core-0.5.0-rc.1-sources.jar) |
| `jakarta.annotation:jakarta.annotation-api:1.3.5`       | [匹配源码](https://repo.maven.apache.org/maven2/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5-sources.jar)       |
| `jakarta.annotation:jakarta.annotation-api:2.1.1`       | [匹配源码](https://repo.maven.apache.org/maven2/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1-sources.jar)       |
| `net.jcip:jcip-annotations:1.0`                         | [匹配源码](https://repo.maven.apache.org/maven2/net/jcip/jcip-annotations/1.0/jcip-annotations-1.0-sources.jar)                                 |
| `org.apache.tomcat.embed:tomcat-embed-core:10.1.24`     | [匹配源码](https://repo.maven.apache.org/maven2/org/apache/tomcat/embed/tomcat-embed-core/10.1.24/tomcat-embed-core-10.1.24-sources.jar)        |
| `org.apache.tomcat.embed:tomcat-embed-core:9.0.83`      | [匹配源码](https://repo.maven.apache.org/maven2/org/apache/tomcat/embed/tomcat-embed-core/9.0.83/tomcat-embed-core-9.0.83-sources.jar)          |

## 普通库与完整应用分别处理

普通 Maven JAR 不捆绑上述外部库的 class，保留项目许可、模块依赖报告、
第三方源码声明和必要许可／源码材料。审计记录 `source-audit.json` 及两份
前端 `*-inventory.json` 留在仓库供复核，不随 JAR 分发；前端清单同时用于
声明生成及构建校验。原始声明、来源链接和随包使用说明继续附带。

根 `NOTICE` 提供项目许可范围和分发材料的指引，模块生成的
`META-INF/THIRD-PARTY-NOTICES.txt` 列出依赖的具体版本和收集到的原始声明。
含 EPL 组件的完整应用还需按下述流程附带适用条款和匹配源码；
只在 `NOTICE` 中列出组件名称不能代替这些分发要求。

包含 jump3r 运行时依赖的普通 JAR 会附带其匹配源码归档，交付这些
JAR 同样涉及库源码的分发；现有核查材料尚未明确 FFT 历史权利说明
与上游 LGPL 声明对该文件的具体适用关系，
发行前应按[1078 说明](license-1078-review.md)针对精确版本确认许可依据。
包含 1078 support 时保留 Symphonia 改编源码。8 个 core/808 发布模块
没有依赖或捆绑 jump3r。

交付包含外部库的 Boot 应用时，单独生成配套材料：

```shell
./gradlew :samples:jt-1078-server-sample-webflux-boot3:prepareDistributionLicenses \
  -Pjt-framework.backend.build.debug-module-fatjar.enabled=true
```

输出为该模块的 `build/reports/distribution-licenses/` 和旁边的
`distribution-licenses.zip`。任务读取实际 Boot JAR 的 `BOOT-INF/lib`，
不会修改应用 JAR，也不会发布。交付时将核对后的 ZIP 与应用一起提供。

材料包含实际内嵌依赖的原始声明、Netty 对应版本的 NOTICE 及引用文本、
所选 Logback 条款、Logback／Jakarta 等匹配源码、Tomcat Schema／DTD，
以及实际使用的 jsr305 原始头、CC-BY-2.5 全文和 jump3r／MPL 材料。
每次输出记录来源、哈希和待核对项；新 Logback 版本提示重新选择分支。

首次运行需要对应依赖源码和上游文本，缓存齐全后可用 `--offline`；
需要 Python 3.9+ 和 curl。普通 jar、sourcesJar、javaDocJar、bootJar、
build 不会自动执行这个额外的应用材料任务。

## 前端和文档的维护入口

依赖更新、声明生成、构建检查及提交范围的操作流程见[维护清单](license-maintenance.md)。

- Dashboard／播放器：`licenses/frontend-inventory.json` 和
  `build-script/generate-frontend-license-notices.py`；原始材料缺项按
  [前端材料说明](../licenses/frontend-mit/README.md)留档，汇总 NOTICE
  已包含取得的原始声明、完整条款和适用署名。
- 文档站点：独立清单和生成命令见[文档许可说明](README.md)，包含搜索
  Worker、Devtools 和 NProgress 的原始声明。构建核对锁文件、浏览器包、
  Worker 来源及声明哈希，输出 `license-build-provenance.json`。
- 远程 Iconfont 已替换为 Font Awesome Free 6.7.2 的 36 个本地实心 SVG，
  覆盖 45 个名称；图形内联进生产 CSS，CC BY 4.0 署名和条款随站点
  声明提供。图标来源及映射见[图标说明](src/.vuepress/styles/icons/README.md)。

## 验证与复核条件

2026-10-17 的本地检查覆盖 51 个 JAR、四个实际 Boot 应用的配套材料、
Dashboard 声明与静态目录、VuePress 121 页生产构建及 45 个图标显示。
这些是已完成的历史检查，不代替最终发行构件的检查。

新增或升级依赖、复制源码、shade、分发容器或完整工具链时，按实际
交付内容复核。本文件的结论限于已核对的版本、文件和分发方式；
三项前端组件的上游 MIT 声明、原始材料范围及处理方式见[前端材料说明](../licenses/frontend-mit/README.md)。
