# jump3r 许可核查记录

说明更新：2026-11-17；以下固定依据和此前验证结果保留原有核查范围。

## 当前使用与未决事项

本项目使用未修改的外部依赖 `de.sciss:jump3r:1.0.5`，其上游 POM 声明
LGPL-2.1-or-later。`de/sciss/jump3r/mp3/FFT.java` 的文件头保留了涉及
GPL 来源及其他历史权利事项的说明；现有材料尚未明确它与上游 LGPL
声明在该实现及后续修改上的具体适用关系。

本项目依据上游 LGPL 声明准备源码和重建／替换材料，不将这些准备视为
FFT 许可问题已解决。原文见 [UPSTREAM-NOTICES.txt](../licenses/jump3r/UPSTREAM-NOTICES.txt)，
构建和替换操作见 [jump3r 使用材料](../licenses/jump3r/README.md)，
应用交付方式见[许可维护](license-maintenance.md#后端)。

## 历史差异的固定依据

jump3r `mp3/Version.java` 的版本常量指向 LAME 3.98.4，这提供对照线索，
不证明全部 Java 文件只来自该版本。原始文字保存在
[UPSTREAM-NOTICES.txt](../licenses/jump3r/UPSTREAM-NOTICES.txt)，
已核对的 Maven Central 1.0.5 二进制和源码 SHA-256 分别为：

- 二进制：`89a5894eee514f9e86a61f01d59b2665af6eddcc25b01accc86514bae16f0054`。
- 源码：`bbd353fcb01d6192b7faf00939cbb4f65e31c6911de4972007af0dd9775614e9`。

| 固定材料                                                                                                  | 已核对的事实与判断                                                                                                                                                               |
|-----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [LAME 3.98.4 官方发布归档](https://downloads.sourceforge.net/project/lame/lame/3.98.4/lame-3.98.4.tar.gz) | mpglib/README 明确写 LGPL；ChangeLog 的 2010-03-22 记录更新解码文件及许可说明，frontend/parse.c 同时仍保留旧 GPL 帮助文字。支持“旧帮助提示未更新”的解释                          |
| 同归档的 libmp3lame/fft.c                                                                                 | 首段与 jump3r FFT.java 在统一换行后相同，包含 Ron Mayer 的历史说明；归档历史文档记录 3.52beta（1999-11-08）曾获版权人同意重新授权，但覆盖当前 FFT 实现及后续修改的范围未单独确认 |
| [Debian LAME 3.100-6 的 copyright](https://sources.debian.org/data/main/l/lame/3.100-6/debian/copyright)  | 将 fft.c 单独分类为 GPL-1+；该分类针对 LAME 3.100-6，其与 jump3r 1.0.5 所含 FFT 实现的适用关系尚待澄清                                                                           |

固定归档 SHA-256：

- LAME 3.98.4：`ac3144c76617223a9be4aaa3e28a66b51bcab28141050c3af04cb06836f772c8`。
- Debian copyright：`f0e4c5098ffedad31473eec38810c26409223858fb8d63dd7685bf225f96bc5d`。

本项目依据上游 LGPL 声明准备分发材料。现有材料尚未明确 FFT 历史
说明与该声明在当前实现及后续修改上的具体关系；相关记录未被作为额外授权。
本项目不将 FFT 文件头中的历史说明视为独立的完整许可，也不据此认定“LGPL 与 GPL 可任选”。
Debian 对其他版本的分类也不直接作为 jump3r 1.0.5 的许可结论。
编码检查实际加载了 mpglib 和 FFT 类，直接删除会导致初始化失败，
因此当前保留未修改的库。

## 此前完成的材料和验证

以下验证记录保留此前核查结果；当前打包方式按上文的独立配套材料处理。

- 匹配源码包含 74 个 Java 文件，二进制的全部顶层 class 均有对应源码；
  重建脚本可以编译这些源码。
- 普通 JAR 不捆绑 jump3r 的 class 或源码；相应 Boot JAR 包含独立嵌套库，
  匹配源码及重建／替换材料由独立配套任务准备。
- 已在一个真实 Boot 示例上验证重建库替换和单声道、8 kHz、16 bit 的
  编码结果。该验证不覆盖所有音频参数或下游打包方式。
- LGPL 文本、原始说明、重建和替换脚本已准备。
  操作步骤见 [jump3r 使用材料](../licenses/jump3r/README.md)。

发行包含 jump3r 源码或二进制的产物前，应针对精确版本形成有依据的
许可判断，必要时向上游确认或替换组件，并检查最终构件和相关条款。
保留未决事项说明、提供源码或通过替换测试均不代替该许可判断。
含 LGPL 库的应用应允许库替换及适用条款要求的调试逆向工程。
上述材料验证 **不等于** 对任意下游商业产品作出许可结论。

## 维护与复核

收到新的许可依据、相反许可信息或权利主张，以及版本、使用或分发方式
变化时，复核受影响范围。日常操作见[许可维护](license-maintenance.md)；
历史贡献确认和正式发行范围分别见[授权记录](license-consents.md)与
[发行清单](license-release-checklist.md)。
