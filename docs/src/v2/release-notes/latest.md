---
icon: branch
---

# 3.x

## 下一版本（尚未发布）

### 许可证与第三方声明（准备中）

- 维护者有权授权的原创 Java 后端代码采用 Apache License 2.0；其他贡献
  按原有许可和权利人的明确授权范围处理。已补充根 LICENSE 和双语
  NOTICE，历史贡献的许可确认尚未完成；项目的许可证声明不扩大
  未确认贡献的授权范围。正式发行前应处理未覆盖的贡献，并确定版本及
  最终许可范围；Maven POM 的许可名称与链接将在发行范围确认后配置。
  已发布的 Maven 构件和 Git 标签保持不变。
- 第三方依赖、复制/改编的源码和前端资源保留各自许可及原作者声明。
  维护者有权授权的原创文档、文档站点代码和示例前端采用 MIT，Dashboard UI 保留 MIT；其他历史贡献按实际授权范围处理。
  docs 中的 Java 示例和本项目自行绘制的 JT 标识及其衍生图形采用 Apache-2.0；历史 MIT 范围仍需
  核对已有授权或贡献者补充确认，不将 Apache-2.0 回复视为 MIT 授权。
  Symphonia 改编的 IMA ADPCM 文件保留 MPL-2.0，
  Checkstyle 改编配置保留 LGPL-2.1-or-later。
- Java 构件附带项目许可、第三方源码/前端声明和所属模块的依赖报告。
  完整 Boot 应用的额外许可/源码材料通过 `prepareDistributionLicenses`
  按需生成配套 ZIP，输出位于 `build/reports/`。
- 当前中央仓库发布名单包含 core 和 808 的 8 个模块。
  1078、Dashboard 和示例暂不加入中央仓库发布名单。
- 新贡献增加 PR 许可确认；已有其他许可的文件继续遵循其原许可。
- 1078 使用未修改的外部依赖 `de.sciss:jump3r:1.0.5`，其上游 POM
  声明许可为 LGPL-2.1-or-later；项目的 Apache-2.0 许可不适用于该依赖。
  该版本中的 `FFT.java` 保留了涉及 GPL 来源及其他历史权利事项的文件头
  说明；现有核查材料尚未明确该历史说明与上游 LGPL 声明对该文件的
  具体适用关系，相关原始声明予以保留。本项目已依据上游 LGPL 声明准备
  许可、源码及重建／替换材料。相关普通 JAR 已附带库源码归档；
  发行包含其源码或二进制的产物前，应针对精确版本确认许可依据。
  分发步骤及未决事项见仓库中的 `docs/license-1078-review.md`。
- 前端产物附带第三方许可及适用署名。示例前端单独附带实际捆绑播放器
  的声明，文档站点附带 MIT 及适用例外的许可文本。部分组件缺失的原始
  材料、已取得的许可依据和版本来源见仓库中的 `licenses/frontend-mit/README.md`。

### 兼容性调整

- `JtWebUtils.getClientIp(headerProvider)` 默认仅依次读取
  `X-Forwarded-For`、`X-Real-IP`，不再自动尝试其他旧代理头别名。
  如需指定头名称及优先级，使用新增重载：
  `JtWebUtils.getClientIp(headerProvider, "HTTP_CLIENT_IP", "X-Real-IP")`。
- 地址候选会去除两端空白；第一个逗号片段为空或为 `unknown` 时跳到
  下一个头，不会使用同一头中后续的代理地址。无候选值时返回空 Optional，
  由调用方继续回退到连接地址。

### 示例日志配置

- 重写 samples、demos 和 adapter 测试中的 17 份旧博客 Logback 配置。
  应用保留原日志目录、按 DEBUG/INFO/WARN/ERROR 分流的文件，以及默认
  100MB / 2 天 / 每级别 2GB 的归档参数；adapter 测试仅输出控制台。
- 新格式使用 `yyyy-MM-dd'T'HH:mm:ss.SSS`，完整显示线程名，并调整列顺序
  和分隔符。控制台保留颜色，文件保留 UTF-8 和异常栈；解析旧格式的脚本
  需要适配。
- 应用日志目录及归档参数支持 `JT_LOG_DIR`、`JT_LOG_MAX_FILE_SIZE`、
  `JT_LOG_MAX_HISTORY`、`JT_LOG_TOTAL_SIZE_CAP` 系统属性或环境变量覆盖。
  控制台和文件格式支持 `CONSOLE_LOG_PATTERN`、`FILE_LOG_PATTERN`。

### 源码说明

- `BitOperator.toUnsignedBigInteger` 改用低 63 位与最高位组合的实现，
  替换此前复制自 OpenJDK 的方法，保持将 long 位模式解释为无符号整数
  的结果，已有边界和随机输入测试。
- IMA ADPCM 转换器保留实际使用的 Symphonia 改编解码路径，按 MPL-2.0
  提供源码；移除未被调用的 `expandNibbleV2` 及其长篇伪代码摘录。
- SPS 解码器的两段 H.264 规范伪代码摘录改为项目编写的简短用途说明，
  保留规范章节引用和第三方 MIT 声明；解析逻辑未改动。

## 3.0.0 (2026-09-12)

### 🐞 Bug Fixes

- 修复 Netty 消息处理线程池被 Spring 误识别为 `ScheduledExecutorService`，导致 `@Scheduled` 任务可能启动失败的问题 [#101](https://github.com/hylexus/jt-framework/issues/101)
  - 808 指令服务器、808 附件服务器和 1078 服务器的三个具名线程池 Bean 改为 `JtEventExecutorGroupProvider`
  - Bean 名和公开常量保持不变，自定义 Bean 只需调整返回类型和注入类型

### ❤️ Contributors

- Codex
- [@hylexus](https://github.com/hylexus)

## 3.0.0-rc.2(2026-01-27)

### 🐞 Bug Fixes

- 下发指令的消息流水号和应答时的消息流水号对应不上 [#97](https://github.com/hylexus/jt-framework/issues/97)

## 3.0.0-rc.1(2026-01-17)

### ⭐ 新特性

适配 **xtream-codec** [#93](https://github.com/hylexus/jt-framework/issues/93)

- jt-808-server-xtream-codec-adapter
- jt-808-server-xtream-codec-adapter-spring-boot-starter
- jt-808-server-xtream-codec-adapter-spring-boot-starter-boot2
