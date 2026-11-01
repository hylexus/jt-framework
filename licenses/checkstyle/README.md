# Checkstyle 构建配置的来源与分发

本目录的 `google_checks-8.23.xml` 与
`build-script/checkstyle/checkstyle.xml`（项目修改版）按 **LGPL-2.1-or-later**
提供，作为仓库默认 Apache-2.0 许可的例外。完整条款见
[`../checkstyle-LICENSE.txt`](../checkstyle-LICENSE.txt)。这里不假设配置文件
另有许可例外，也不将其改为 Apache-2.0。

## 原始来源

- 原始配置署名：Max Vetrenko、Ruslan Diachenko、Roman Ivanov。
- 固定对照版本：[Checkstyle 8.23 的 google_checks.xml](https://github.com/checkstyle/checkstyle/blob/checkstyle-8.23/src/main/resources/google_checks.xml)。
- [同版本 POM](https://github.com/checkstyle/checkstyle/blob/checkstyle-8.23/pom.xml)
  的许可名称为 `LGPL-2.1+`。
- [同版本 LICENSE](https://github.com/checkstyle/checkstyle/blob/checkstyle-8.23/LICENSE)
  是 GNU LGPL 2.1 文本，保留在本仓库的 `licenses/checkstyle-LICENSE.txt`。
- `google_checks-8.23.xml` 保存官方原始文件，未修改其内容；下载来源及
  SHA-256 见 `provenance.json`。

8.23 是用于核对的固定版本，并非对历史复制版本的断言。本项目首次引入
Checkstyle 配置的记录为 2019-08-21 的 `9407eb60`；此文件后续修改的
Git 作者记录均为 hylexus。

## 项目已有修改

对比 8.23 的配置，项目修改版主要包含：

- severity 从 warning 改为 error，缩进采用 4 空格，续行采用 8 空格。
- 增加外部 `SuppressionFilter` 和 `SuppressWarningsFilter` /
  `SuppressWarningsHolder`；外部抑制规则仅针对 G.7xx 移植代码。
- 调整 Unicode 转义和 Javadoc 检查参数。
- 不启用原配置中的星号导入、导入排序、行长、缺失 Javadoc、Javadoc
  段落/续行和摘要等部分可选规则。
- 使用兼容现有工具版本的 TreeWalker 和 JavadocMethod 类名。

这是一份修改过的上游配置，不声称完全遵循 Google Java Style。
2026-10-03 的许可整理只补充许可、来源和修改记录，未改动任何有效 XML
元素、属性、消息或规则顺序；61 个已启用 module 节点（包括 Checker、
TreeWalker 和过滤器）保留。

## 源码与构件中的位置

仓库内的项目修改版源文件为 `build-script/checkstyle/checkstyle.xml`，
可直接编辑。`build-script/checkstyle/checkstyle-suppressions.xml` 是项目
自己的抑制配置，源码也在仓库内。

本项目生成的 JAR 将随附下列许可资料：

- `META-INF/licenses/checkstyle-LICENSE.txt`：完整 LGPL 条款。
- `META-INF/licenses/checkstyle/google_checks-8.23.xml`：上游对照源文件。
- `META-INF/licenses/checkstyle/jt-framework-checkstyle.xml`：构建时直接
  从实际使用的项目修改版读取，避免维护一份不同步的配置副本。
- `META-INF/licenses/checkstyle/README.md` 和 `provenance.json`：来源及修改说明。

它们是许可资料中的可编辑文本，不是应用运行时的 Checkstyle 类或配置。
项目的 Java 类仍按各自源码声明提供。构建工具库本身的版本和依赖应单独
核对，不由此处的 8.23 对照版本决定。

## 后续维护

修改这份衍生配置时，继续保留原作者、LGPL 条款及来源说明，记录修改日期
和内容；不要由全仓库的 Apache 版权头脚本覆盖。随 JAR 附带的修改版由
Gradle 直接读取当前文件，原始对照文件保持不变。
