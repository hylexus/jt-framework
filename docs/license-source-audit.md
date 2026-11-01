# 第三方源码与资源说明

更新日期：2026-10-24。本文件记录当前源码的来源、许可处理和相关行为变化。

核对基线为 `2f882f5bba93aa4bf4a63f06225e31e4d759d51d` 及许可证整理
涉及的文件变更，覆盖 core、808、1078、Dashboard、示例、文档和构建配置。
核对结合 Git 历史、上游源码归档及文本／token 对照，相似片段作为来源
核查线索，结合原始文件及实际用法判断。记录限于已核对材料；固定
对照版本未必等于准确的历史引入版本。

## 声明和来源的保留位置

- [第三方源码声明](../THIRD-PARTY-SOURCE-NOTICES.txt)：随包提供的来源、
  适用文件、原始署名、许可和修改说明；文件自身的声明继续保留。
- [来源链接](../licenses/source-provenance.json)：固定上游文件 URL。
- [来源审计记录](../licenses/source-audit.json)：历史／当前文件哈希和
  对照材料，仅供仓库复核，不放进 JAR；历史记录不表示旧版本已获授权。
- `licenses/`：原始许可、NOTICE、必要的上游材料及使用说明。
- [授权记录](license-consents.md)：历史贡献者的实际确认及覆盖范围；
  自有代码采用 Apache-2.0 的意向不替代第三方或其他贡献者的授权。

## 当前保留的第三方来源

| 来源及固定对照                              | 本地范围                                                                    | 保留的许可与处理                                                                                       |
|---------------------------------------------|-----------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| Spring Framework 5.3.14／5.3.27／5.3.31     | 808 过滤器链、拦截器、HandlerExecutionChain；Request／Exchange 属性辅助方法 | Apache-2.0，原始文件版权头、对应方法范围和修改说明                                                     |
| Spring Boot Admin，83db63e…／1433ca5…       | Dashboard 注册器、监听器、客户端、代理、注册表和指标调度                    | Apache-2.0，保留上游署名；准确文件链接见来源记录                                                       |
| Commons Codec 1.11                          | core 的 HexStringUtils 编码／解码及 digit 处理                              | Apache-2.0，ASF 版权头、修改说明和原始 NOTICE；其中未采用的测试数据不视为本项目组成                    |
| matrixy/jtt1078-video-server，2d425b6…      | 1078 音频转换集成和 DefaultFlvEncoder 部分逻辑                              | Apache-2.0，matrixy 声明、相关 houcheng 署名及修改说明；底层算法分别处理                               |
| Sun G.7xx                                   | G711、G726_16／24／32／40、G72X 及包说明                                    | 保留 Sun 原始许可、版权及无担保声明                                                                    |
| Symphonia，b157bb5…                         | IMA ADPCM 的实际解码路径                                                    | MPL-2.0，保留原始署名，提供当前改编 Java 源码；移除未调用的 expandNibbleV2 和伪代码摘录                |
| jmuxer，579835d…；SmallChi/JT1078，c4931a8… | H.264 状态机、SPS 的部分流程和 NALU 元数据                                  | MIT，保留 Samir Das、SmallChi(Koike) 署名及原始条款                                                    |
| EyerH264Decoder，59e0a39…                   | DefaultSpsDecoder、BitStreamReader、DefaultBitStreamReader                  | MIT，Redknot 原始署名、BitStream.cpp／NaluSPS.cpp 固定来源及修改说明                                   |
| xtream-codec                                | 已改编的工具／适配代码                                                      | 保留已有 Apache-2.0 声明；仍保留其中更早的第三方来源                                                   |
| vue-element-admin 4.4.0                     | Dashboard request.ts                                                        | MIT，PanJiaChen 署名、Axios 拦截器来源及修改说明                                                       |
| create-vue v3.7.2                           | Dashboard counter.ts                                                        | MIT，vuejs 原始声明                                                                                    |
| vuepress-theme-hope v2.0.0-rc.32            | 文档 RepoGitee.js、repo-link.css                                            | MIT，Mr.Hope 原始声明及改编说明                                                                        |
| element-plus-vite-starter，fa10f32…         | Dashboard 布局、composables、样式及部分 Vite 结构                           | 按上游 MIT 元数据处理，保留固定 package.json 和补充条款，缺失原始材料留档                              |
| Checkstyle 8.23 的 google_checks.xml        | build-script/checkstyle/checkstyle.xml                                      | LGPL-2.1-or-later，Max Vetrenko、Ruslan Diachenko、Roman Ivanov 原始署名、修改说明、全文及原始对照配置 |
| mpegts.js 1.7.3、Element Plus icons 2.1.0   | 示例播放器、Dashboard 经 UnoCSS 使用的图标数据                              | 保留对应前端原始许可和署名；开发依赖中的数据实际进入产物时也纳入                                       |
| OpenSpec 1.13.0                             | 仓库内生成的工具模板                                                        | 保留模板自身 MIT 元数据和原始许可；不作为维护者原创 Apache 代码处理                                    |

jump3r 是未修改的外部依赖，具体历史差异、源码和替换步骤见
[1078 说明](license-1078-review.md)。JVM 和前端依赖的使用／分发范围见
[依赖说明](license-jvm-review.md)。

## 已替换的来源及行为变化

### 客户端 IP 提取

旧 JtWebUtils 的头列表和选择流程对应
[panser 的 Stack Overflow 回答](https://stackoverflow.com/a/55266907)，
页面适用 CC BY-SA 4.0。当前已移除旧的 12 项头列表及实现，按项目需求
重新编写；新实现采用 Apache-2.0，旧来源与哈希继续留档。

- 保留的单参数入口默认按 `X-Forwarded-For`、`X-Real-IP` 顺序读取。
- 新重载只读取调用者指定的头及顺序，空数组不读取头，不追加默认项。
- 每个头只取首个逗号前的文字并 trim；null、空值和忽略大小写的
  unknown 跳过，不把同一头后面的代理地址作为客户端地址。
- 不解析 DNS 或验证 IP 格式；原有连接地址 fallback 保留。可信代理
  需要设置／覆盖这些头，不能仅凭头文字鉴权。

旧别名如 HTTP_CLIENT_IP 不再默认识别，可通过重载显式传入。
功能说明参考 [NGINX](https://nginx.org/en/docs/http/ngx_http_proxy_module.html)
和 [Apache HTTP Server](https://httpd.apache.org/docs/2.4/mod/mod_remoteip.html)，
未复制其程序或整段文档。兼容变化已记入[发行说明](src/v2/release-notes/latest.md)。

### 17 份 Logback 配置

旧配置来自 [CSDN](https://blog.csdn.net/xu_san_duo/article/details/80364600)
和 [ITEye 转载](https://www.iteye.com/blog/cgi-bin-2395647)。前者声明
CC BY-SA 4.0，后者未取得覆盖全部片段的明确许可。当前已按项目需求
重写配置，移除旧说明及配置块；历史名单和哈希保留在来源审计记录。

16 个应用保留控制台及四个按确切级别分流的日志文件、原默认目录、
100MB 大小阈值、2 天归档和每级别 2GB 归档上限；adapter 测试只保留
控制台。时间格式、列布局和可覆盖参数有变化，具体参数及兼容说明见
[发行说明](src/v2/release-notes/latest.md)。新项目配置采用 Apache-2.0，
Logback 库自身仍保留原许可。

### 无符号转换、规范摘录和图形

- BitOperator.toUnsignedBigInteger 已改用低 63 位与最高位组合的实现，
  替换此前复制自 OpenJDK 的方法，保持无符号结果；边界和随机位模式有测试。
- DefaultSpsDecoder 中两段 H.264 规范伪代码已删除，保留项目编写的
  用途说明和 H.264 (08/2021) 章节链接；执行逻辑和原有 MIT 声明保留。
- 来源尚未确认的旧立方体图标，以及此前使用的 VuePress logo／hero，
  均已替换为项目几何 JT 标识；VuePress 图片的已知 MIT 来源继续留档。
  [品牌目录](../assets/brand/README.md)保留可编辑 SVG、全部 PNG、
  manifest 和生成步骤，图形采用 Apache-2.0。
- 文档远程 Iconfont 已替换为 Font Awesome Free 6.7.2 的 36 个本地
  实心 SVG，覆盖原有 45 个名称，图标保留 CC BY 4.0 及原始署名；
  图形未修改，只按项目映射重命名，通过 CSS mask 使用文字颜色。
  固定版本、来源、哈希及许可见[本地图标说明](src/.vuepress/styles/icons/README.md)。
  VuePress 旧图片的原始 MIT 文字和历史哈希仍保留作来源记录。
- vuepress-theme-hope v2.0.0-rc.32 的
  [crimson.css](https://github.com/vuepress-theme-hope/vuepress-theme-hope/blob/v2.0.0-rc.32/packages/theme/src/client/styles/fonts/crimson.css)
  内嵌 Crimson 数字字体子集；该子集未找到足以确认其字体许可的材料。
  原 CSS 的 SHA-256 为 `bfdcd5b0929cb85afb4c2f07d369504cf6e36a94787b54ca8e7a63e5937534cd`。
  当前通过 PostCSS 移除该字体定义，标题改用系统字体回退；构建不再
  分发该字体数据。主题其他代码仍按已有 MIT 声明使用。

这些处理只说明当前源码和资源的使用方式，不追溯改变历史版本或原作品授权。

## 持续维护

Gradle 直接从 `build-script/checkstyle/checkstyle.xml` 收集当前可编辑配置，
与原始对照配置和 LGPL 条款一并放入 JAR。

lodash-unified、webworkify-webpack、element-plus-vite-starter 的已有
MIT 声明、取得的原始文字、完整条款和适用署名已进入汇总 NOTICE；
未找到的原始版权文字／独立许可文件按[材料说明](../licenses/frontend-mit/README.md)
留档。有新的适用证据、相反许可信息或使用方式变化时再复核，
不推测版权人和年份。

新增复制来源时保留文件头、原始许可、固定链接和修改范围；修改依赖、
声明或打包方式后检查受影响构件。当前授权与正式发行事项统一维护在
[发行清单](license-release-checklist.md)。
