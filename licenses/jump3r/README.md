# jump3r 源码与重新构建 / Sources and rebuilding

MP3 编码使用未修改的外部依赖 `de.sciss:jump3r:1.0.5`，上游 POM 声明
LGPL-2.1-or-later；项目的 Apache-2.0 许可不适用于该依赖。FFT 文件的
历史权利说明尚未明确，固定依据和状态集中见仓库的
[核查记录](https://github.com/hylexus/jt-framework/blob/master/docs/license-1078-review.md)，
原文见 [UPSTREAM-NOTICES.txt](UPSTREAM-NOTICES.txt)。

普通库由 Gradle 解析依赖，不附带 jump3r 二进制或源码。实际捆绑它的
Boot 应用通过可选 `prepareDistributionLicenses` 任务获取匹配版本的
Maven sources JAR，与本目录的说明和工具一起放入
`build/reports/distribution-licenses/sources/jump3r/`；LGPL 全文位于配套目录的
`licenses/LGPL-2.1.txt`。目录及同级 ZIP 核对后随应用交付，任务用法见仓库
[许可维护](https://github.com/hylexus/jt-framework/blob/master/docs/license-maintenance.md#后端)。

MP3 encoding uses the unmodified external dependency `de.sciss:jump3r:1.0.5`, whose upstream POM
declares LGPL-2.1-or-later. The project's Apache-2.0 license does not apply to it. The applicability
of the FFT header's historical rights statements remains unresolved; see the repository's
[review record](https://github.com/hylexus/jt-framework/blob/master/docs/license-1078-review.md)
and [original upstream statements](UPSTREAM-NOTICES.txt).

Ordinary libraries resolve jump3r through Gradle and do not bundle its binaries or sources.
For Boot applications bundling it, the optional `prepareDistributionLicenses` task places the matching
Maven sources JAR, these instructions and tools in `build/reports/distribution-licenses/sources/jump3r/`.
The companion bundle includes `licenses/LGPL-2.1.txt`. Review and distribute the directory or ZIP alongside
the application; see [license maintenance](https://github.com/hylexus/jt-framework/blob/master/docs/license-maintenance.md#后端).

## 从源码重新构建 / Rebuild from source

解压配套 ZIP 后，进入 `sources/jump3r/`，使用 Python 3.9+ 和 JDK 11 或后续版本运行：

Extract the companion ZIP, enter `sources/jump3r/`, and run with Python 3.9+ and JDK 11 or later:

```shell
python3 rebuild.py jump3r-1.0.5-sources.jar --output jump3r-1.0.5-rebuilt.jar
```

脚本把发布的 Java 源码编译为 Java 8 字节码，再打包为可替换的 JAR。
它用于提供可检查、可修改的构建方法，不承诺和上游 JAR 字节完全相同。
版本 `1.0.5` 的源码中有原有编译警告；构建不会因此失败。

The script compiles the published Java sources to Java 8 bytecode and packages a replacement JAR.
It provides a build process users can inspect and modify, without promising byte-for-byte identity
with the upstream JAR. The original compiler warnings in version `1.0.5` do not fail the build.

## 在 Spring Boot 应用中替换 jump3r / Replace jump3r in a Spring Boot application

对本项目生成的普通 Spring Boot 可执行 JAR，可用随附脚本生成一个
替换了 jump3r 的副本。在 `sources/jump3r/` 中执行以下命令，
将 `/path/to/jt-1078-server-sample-webflux-boot3.jar` 换成应用 JAR 的实际路径。

For a standard Spring Boot executable JAR built by this project, the supplied script creates a copy
with jump3r replaced. Run the following commands from `sources/jump3r/` and replace
`/path/to/jt-1078-server-sample-webflux-boot3.jar` with the path to your application JAR.

```shell
python3 replace-boot-library.py "/path/to/jt-1078-server-sample-webflux-boot3.jar" \
  jump3r-1.0.5-rebuilt.jar --output jt-1078-with-rebuilt-jump3r.jar
java -jar jt-1078-with-rebuilt-jump3r.jar
```

脚本保留原应用文件，替换 `BOOT-INF/lib/jump3r-*.jar` 中唯一匹配的库，
保留嵌套文件名、其他归档内容及索引，并将替换的库以不压缩方式写入，
供 Spring Boot 加载。输出路径必须是新文件。选择与应用接口兼容的
库版本，修改库后应验证实际使用的编码参数及应用功能。
Spring Boot 3 的空 `META-INF/BOOT.SF` 标记会保留；它不是 JAR 签名。
此脚本只替换库二进制。若要再分发使用修改版库的应用，还需提供修改版
库的匹配源码和构建材料，并更新说明；原有配套源码材料不会自动更新。

The script preserves the original application and replaces the single matching `BOOT-INF/lib/jump3r-*.jar`.
It retains the nested filename, other archive contents and indexes, and stores the replacement library
uncompressed for Spring Boot's loader. Choose a new output path and an interface-compatible library;
verify the application's functions and the encoding parameters you use after modifying the library.
Spring Boot 3's empty `META-INF/BOOT.SF` marker is retained; it is not a JAR signature.
Only the binary is replaced. Redistributing an application with a modified library also requires matching
modified sources, build materials and updated notices; the original companion materials are not updated automatically.

带 JAR 签名、前置 shell 启动脚本或不同打包结构的应用不适用此脚本；
这些情况需要发行方提供对应的重建/签名或替换流程。普通 Maven 库的
使用者可以在其应用构建中使用重建的 jump3r，并重新构建应用。

The script does not support signed JARs, shell-prefixed executables or other packaging structures;
distributors must provide an appropriate rebuild/signing or replacement process for those cases.
Users of ordinary Maven libraries can use the rebuilt jump3r in their application build and rebuild the application.

完整 LGPL 文本位于仓库的 `licenses/LGPL-2.1.txt`，以及配套目录的
`licenses/LGPL-2.1.txt`。上游信息：

The full LGPL text is in `licenses/LGPL-2.1.txt` in both the repository and the companion bundle.
Upstream information:

- https://sourceforge.net/projects/jump3r/
- https://git.iem.at/sciss/jump3r
- https://repo.maven.apache.org/maven2/de/sciss/jump3r/

按 LGPL 分发包含 jump3r 的 Spring Boot 应用时，应保留适用的许可和版权
声明，提供匹配的库源码、构建材料及实际可用的替换方法，使用户能够使用
修改后的、接口兼容的库运行应用。应用条款应允许 LGPL 要求的自用修改，
以及为调试这些修改所需的逆向工程。具体义务取决于库是否修改、打包方式
和实际交付内容。

When distributing a Spring Boot application containing jump3r under the LGPL, retain the applicable
license and copyright notices, and provide matching library sources, build materials and a working
replacement method so users can run the application with an interface-compatible modified library.
Application terms must allow modification for the user's own use and reverse engineering for debugging
those modifications as required by the LGPL. Specific obligations depend on library modifications,
packaging and the actual distribution.

本目录是发行准备材料，不是对任何下游商业产品的自动许可审查结论。

These materials are for distribution preparation; they are not a license assessment of any downstream commercial product.
