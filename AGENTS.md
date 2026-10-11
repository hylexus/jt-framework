# AGENTS.md

本文件适用于仓库根目录及其子目录，为在本项目中工作的编码代理提供约定。执行任务时结合用户要求、相关模块代码和 OpenSpec 规格；子目录有自己的 `AGENTS.md` 时，同时遵循其适用范围内的约定。

## 项目与目录

jt-framework 是基于 Java、Spring Boot 和 Netty 的 JT/T 808、JT/T 1078 协议框架，采用 Gradle Kotlin DSL 多模块构建。

| 路径 | 用途 |
| --- | --- |
| `jt-core/` | 公共接口、工具和基础设施 |
| `jt-808-server-support/`、`jt-1078-server-support/` | 协议实现、编解码、消息处理和会话管理 |
| `jt-*-server-spring-boot-autoconfigure/`、`jt-*-server-spring-boot-starter*/` | Spring Boot 自动配置及不同版本的 starter |
| `jt-808-server-xtream-codec-adapter*/` | xtream-codec 适配及其 starter |
| `dashboard/` | 管理服务、客户端及 Vue 3 前端 |
| `samples/`、`demos/` | 示例应用和演示项目 |
| `docs/` | VuePress 文档；当前功能文档主要位于 `docs/src/v2/` |
| `buildSrc/`、`build-script/` | 构建逻辑、兼容矩阵和 Checkstyle 配置 |
| `openspec/` | 主规格、活跃变更及归档记录 |

模块构建文件采用 `<模块名>.gradle.kts` 命名，由 `settings.gradle.kts` 配置。修改前先查看受影响模块的构建文件和依赖关系。

## 环境与兼容性

- 使用仓库的 `./gradlew`；Windows 使用 `gradlew.bat`。Gradle 版本以 `gradle/wrapper/gradle-wrapper.properties` 为准。
- 开发环境由 `mise.toml` 声明，目前为 Temurin JDK 21 和 Node.js 20；CI 也使用 JDK 21 构建。
- 构建所用 JDK 与模块的最低 Java 版本是两个独立约束。模块兼容性以 `buildSrc/src/main/kotlin/JtFrameworkVersions.kt` 中的 `COMPATIBILITY_MATRIX` 为准。

| 模块范围 | Java 编译/API 目标 | Spring Boot 系列 |
| --- | --- | --- |
| `jt-core`、808/1078 support、autoconfigure、普通 `*-starter-boot2` | Java 8 | 依赖管理使用 Boot 2 |
| 普通 Boot 3 starter、dashboard、Boot 3 samples/demos | Java 17 | Boot 3 |
| xtream-codec adapter、其 starter 和 adapter samples | Java 21 | 按模块分别使用 Boot 2 或 Boot 3 |

Java 8 模块中的生产代码必须使用 Java 8 支持的语法和标准库 API。更改公共模块时，同时考虑其 Boot 2 和 Boot 3 使用方；不要以本机 JDK 21 编译成功作为最低版本兼容性的证明。

## 修改约定

- 优先用中文沟通，说明具体改动、验证结果和仍未解决的问题。
- 开始修改前查看 `git status`，保留用户已有的修改和暂存状态；将改动限制在当前任务范围内。
- 保持既有公开 API 的源码与二进制兼容性，以及配置项、Bean 名称和扩展接口的契约。涉及明确要求的契约调整时，同步更新相应规格和使用说明。
- 修复注解编解码问题时，保持注解成员、默认值、元注解、别名以及字段顺序、条件和长度求值规则。默认类型、重复 key 覆盖顺序、结果可修改性、转换器优先级与复用生命周期也属于既有语义。
- 保持既有非竞态错误的异常类型、包装和传播方式；不要在修复中顺带新增注解配置校验、依赖升级或应用迁移要求。
- 遵循附近代码的格式、命名和导入习惯；Java 通常使用四空格缩进，文件使用 UTF-8。复用现有工具类和依赖管理。
- 注释简要说明原因、并发前提或资源所有权。保留已有 Javadoc 和作者信息。
- 共享缓存必须安全发布完整构造的状态。使用并发容器时，仍需判断复合操作的原子性和缓存值是否会被修改，不能只依据容器类型认定整个流程线程安全。
- 修改 Netty `ByteBuf` 处理时，核对读取位置、切片/复制语义、引用计数和释放责任，覆盖成功及异常路径。共享缓存不应保留请求、缓冲区或请求上下文。
- 调整线程或执行器时，核对 Netty event loop 的阻塞行为、消息处理顺序、Spring 自动配置和执行器关闭生命周期。

## 测试与构建

测试使用 JUnit Jupiter，放在对应模块的 `src/test/java/`。先运行受影响的测试，再按依赖和兼容性风险扩展到模块检查或集成验证。

```bash
# 单个回归测试示例
./gradlew :jt-808-server-support:test --tests 'io.github.hylexus.jt.jt808.support.annotation.codec.Jt808ConverterCacheTest'

# 完整协议支持模块测试
./gradlew :jt-808-server-support:test

# 模块检查，显式启用 Checkstyle
./gradlew :jt-808-server-support:check -Pjt-framework.backend.build.checkstyle.enabled=true
```

需要完整构建时，使用与 CI 一致的开关关闭发布仓库和签名配置：

```bash
./gradlew clean build \
  -Pjt-framework.maven.repo.central-portal.enabled=false \
  -Pjt-framework.maven.repo.private.enabled=false \
  -Pjt-framework.maven.repo.github.enabled=false \
  -Pjt-framework.maven.publications.signing=off \
  -Pjt-framework.backend.build.checkstyle.enabled=true \
  -Pjt-framework.backend.build.debug-module-fatjar.enabled=false
```

构建配置读取优先级为环境变量、Gradle project properties、JVM system properties；环境变量名将属性名中的 `.`、`-` 转换为 `_` 并转为大写。开关未按预期生效时，检查对应环境变量。

- 行为修复添加能检测原问题的回归测试；断言外部可观察的类型、值、报文字节、异常或资源生命周期。
- 并发测试使用屏障或可控的初始化窗口，设置超时并收集所有任务结果；确保工作线程异常能使测试失败。
- 修改共享支持模块的兼容性相关代码时，按受影响范围验证 Java 8、Java 17 及 Boot 2/3 使用方。跨 JDK 运行测试时，检查模块是否向测试 JVM 透传了 Gradle JVM 的 `java.*` 等系统属性。
- 记录实际执行的命令和结果。工具链或环境受限时明确说明未执行项；区分编译兼容检查和实际运行验证。
- 纯文档或注释修改检查内容、路径和空白即可。完成前检查 `git diff --check`；新文件也需检查尾随空格和文件末尾换行。

## 前端与文档

- `docs/` 和 `dashboard/jt-dashboard-server-ui-vue3/` 各自有 `package.json`、`pnpm-lock.yaml`；使用各自目录的脚本和锁文件。
- 文档：在 `docs/` 运行 `pnpm docs:dev` 或 `pnpm docs:build`。其 Node.js、pnpm 最低版本以 `docs/package.json` 为准。
- Dashboard UI：在 `dashboard/jt-dashboard-server-ui-vue3/` 运行 `pnpm dev`；改动后按需执行 `pnpm typecheck` 和 `pnpm build`。
- UI 的 `pnpm lint` 和 `pnpm format` 会修改文件，执行后检查 diff，控制格式化范围。
- 修改当前功能文档时使用 `docs/src/v2/`，核对涉及的示例和 VuePress 导航。`docs/src/v1/` 保留历史版本说明。

## OpenSpec

- 当前 schema 为 `spec-driven`，配置在 `openspec/config.yaml`。主规格位于 `openspec/specs/`；活跃变更位于 `openspec/changes/`；归档位于 `openspec/changes/archive/`。
- 用户调用 OpenSpec 技能时，先读取 `.agents/skills/<技能名>/SKILL.md`，结合当前会话选择对应变更；路径和范围以 CLI 返回的上下文为准。
- 修改相关能力前阅读已有主规格；实施规划好的变更时，按其 proposal、design、specs 和 tasks 执行，任务完成并验证后再勾选。
- 变更验证使用 `openspec validate <change-name> --strict`；同步主规格后使用 `openspec validate --specs`。
- 同步规格和归档是独立步骤，按用户指令执行。合并时保留未涉及的要求和场景；主规格统一使用 `## Requirements`，不保留增量操作标题。
- 验收记录包含已执行的证据和限制；生产事故的原因只有在证据支持时才写成已确认或已复现。
