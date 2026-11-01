# jump3r 源码与重新构建

MP3 编码使用未修改的外部依赖 `de.sciss:jump3r:1.0.5`。其上游 POM
声明许可为 LGPL-2.1-or-later；本项目的 Apache-2.0 许可不适用于该依赖。
本项目已依据上游 LGPL 声明准备许可、源码及重建／替换材料。

该版本中的 `de/sciss/jump3r/mp3/FFT.java` 保留了涉及 GPL 来源及其他
历史权利事项的文件头说明，原文见 [UPSTREAM-NOTICES.txt](UPSTREAM-NOTICES.txt)。
现有核查材料尚未明确该历史说明与上游 LGPL 声明对该文件的具体适用
关系。本项目保留相关原始声明，核查依据及未确定事项记录在仓库的
`docs/license-1078-review.md` 中。

Git 源码仓库中的 Gradle 依赖由使用者构建时从 Maven 下载，不要求
手工安装，也没有将 jump3r 的实现复制为项目自有源码。构建生成的
相关普通 JAR 已附带 jump3r 的源码归档，交付这些 JAR 同样涉及库源码
的分发；Boot JAR 还会包含库二进制。发行包含库源码或二进制的产物前，
应针对精确版本确认许可依据，并保留适用的原始声明；包含库二进制的
应用还需提供匹配源码和可用的构建／替换材料。

构建会从**当前模块实际解析的运行时依赖版本**获取 Maven `sources` JAR，
放入 `META-INF/third-party-sources/jump3r/`，并附带 `rebuild.py`。
依赖版本改变时，源码版本随之改变；源码获取失败会使打包失败。
当前使用的版本是 `1.0.5`，原始二进制和源码均未在本项目中修改。

## 当前版本的源码核对

Maven Central 1.0.5 的二进制和源码 SHA-256 分别为：

- 二进制：`89a5894eee514f9e86a61f01d59b2665af6eddcc25b01accc86514bae16f0054`
- 源码：`bbd353fcb01d6192b7faf00939cbb4f65e31c6911de4972007af0dd9775614e9`

源码归档包含 74 个 Java 文件，二进制中的全部顶层 class 均有对应
Java 文件。`rebuild.py` 可以直接编译这些源码，不需要生成步骤或额外
资源；原始源码中的版权和许可说明通过完整源码归档保留。

当前保留原始、未修改的依赖，分发材料依据上游 LGPL-2.1-or-later 声明准备。
LAME 3.98.4 的 README、源码头和 2010-03-22 变更记录支持
`Parse.java` 中 mpglib GPL 帮助文字未同步更新的解释。
`mp3/FFT.java` 的历史权利说明与 LAME 3.98.4 对应源文件的文件头一致。
固定版本依据及核查范围见仓库的 `docs/license-1078-review.md`。

上述历史说明 **不被作为** LGPL 与 GPL 可任选的授权；保留声明及提供分发材料不等于取得额外授权。
发行包含该库源码或二进制的产物前，应针对精确版本形成有依据的许可
判断，必要时向上游确认或替换组件，并检查实际构件及替换方法。
取得新的许可依据、收到权利主张或改变版本及使用方式时，也应复核。

## 从源码重新构建

将该目录的内容解压到同一个目录，使用 Python 3.9+ 和 JDK 11 或后续版本运行：

```shell
python3 rebuild.py jump3r-1.0.5-sources.jar --output jump3r-1.0.5-rebuilt.jar
```

脚本把发布的 Java 源码编译为 Java 8 字节码，再打包为可替换的 JAR。
它用于提供可检查、可修改的构建方法，不承诺和上游 JAR 字节完全相同。
版本 `1.0.5` 的源码中有原有编译警告；构建不会因此失败。

## 在 Spring Boot 应用中替换 jump3r

对本项目生成的普通 Spring Boot 可执行 JAR，可用随附脚本生成一个
替换了 jump3r 的副本。下面以 1078 WebFlux 示例为例：

```shell
python3 replace-boot-library.py jt-1078-server-sample-webflux-boot3.jar \
  jump3r-1.0.5-rebuilt.jar --output jt-1078-with-rebuilt-jump3r.jar
java -jar jt-1078-with-rebuilt-jump3r.jar
```

脚本保留原应用文件，替换 `BOOT-INF/lib/jump3r-*.jar` 中唯一匹配的库，
保留嵌套文件名、其他归档内容及索引，并将替换的库以不压缩方式写入，
供 Spring Boot 加载。输出路径必须是新文件。选择与应用接口兼容的
库版本，修改库后应验证实际使用的编码参数及应用功能。
Spring Boot 3 的空 `META-INF/BOOT.SF` 标记会保留；它不是 JAR 签名。
此脚本只替换库二进制。若要再分发使用修改版库的应用，还需提供修改版
库的匹配源码和构建材料，并更新说明；应用内原有源码归档不会自动更新。

带 JAR 签名、前置 shell 启动脚本或不同打包结构的应用不适用此脚本；
这些情况需要发行方提供对应的重建/签名或替换流程。普通 Maven 库的
使用者可以在其应用构建中使用重建的 jump3r，并重新构建应用。

完整 LGPL 文本位于仓库的 `licenses/LGPL-2.1.txt`，打包后位于
`META-INF/licenses/LGPL-2.1.txt`。上游信息：

- https://sourceforge.net/projects/jump3r/
- https://git.iem.at/sciss/jump3r
- https://repo.maven.apache.org/maven2/de/sciss/jump3r/

按 LGPL 分发包含 jump3r 的 Spring Boot 应用时，应保留适用的许可和版权
声明，提供匹配的库源码、构建材料及实际可用的替换方法，使用户能够使用
修改后的、接口兼容的库运行应用。应用条款应允许 LGPL 要求的自用修改，
以及为调试这些修改所需的逆向工程。具体义务取决于库是否修改、打包方式
和实际交付内容。

本目录是发行准备材料，不是对任何下游商业产品的自动许可审查结论。
