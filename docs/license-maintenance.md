# 许可维护

日常维护以依赖配置、源文件原始声明和实际交付内容为准。无需维护第三方依赖的
逐文件清单、源码哈希、锁文件哈希或产物哈希。

## 后端

普通库采用统一的 `META-INF/LICENSE.txt` 和 `META-INF/NOTICE`。源码来源集中
维护在根目录 [NOTICE](../NOTICE)，复制源码的版权、MIT 条款、Sun 声明均合并在其中；
不把前端、Checkstyle、历史调查材料或所有传递依赖的声明复制到每个 JAR。
`jt-1078-server-support` 自身附带 MPL 覆盖的 Symphonia 改编文件源码。

常规依赖升级更新依赖配置并执行相关测试和构建；许可或分发要求变化时，同步更新
适用声明和交付材料。需要查看依赖许可声明时，可按需生成报告：

```sh
./gradlew :generateLicenseReport
# 或仅指定模块
./gradlew :jt-1078-server-support:generateLicenseReport
```

报告写入 `build/reports/dependency-license/`，不进入普通打包链。
根任务汇总 Java 模块，也可执行单个模块的同名任务。报告包含 HTML、JSON 和文本，
汇总编译、运行及注解处理器的依赖声明；不扫描测试、前端、构建插件或复制源码。
Unknown、多重许可及仅编译可见的条目需结合实际交付内容理解。

完整 Boot 应用捆绑了外部库，交付时按实际内容提供原始声明、适用源码和替换材料。
原始库声明保留在未修改的嵌套 JAR 中，可按需准备补充目录和 ZIP：

```sh
./gradlew :samples:jt-1078-server-sample-webflux-boot3:prepareDistributionLicenses \
  -Pjt-framework.backend.build.debug-module-fatjar.enabled=true
```

任务仅为需要额外源码或声明的组件准备补充材料：完整交付匹配版本的源码包，
保留其中的原始版权和条款；Netty、Logback 等 Maven 构件缺少的声明从对应
上游发行版本补充。缺少缓存时需要联网获取匹配源码和上游声明，缓存齐全后可用 `--offline`。
依赖报告也随目录交付，实际范围仍以 Boot JAR 为准。
不再逐 Java 文件提取注释、复制已有嵌套声明、自动选择许可或生成哈希清单。

输出留在模块 `build/reports/distribution-licenses/` 及同级 ZIP 中，核对后随应用交付。
1078 模块内的 MPL 改编源码继续随自身 JAR 提供，不在补充目录重复复制。
jump3r 的源码、原始说明和重建／替换工具位于补充目录的 `sources/jump3r/`；
操作见 [jump3r 使用材料](../licenses/jump3r/README.md)，未确定事项集中见
[核查记录](license-1078-review.md)。生成材料不改变其授权状态，也不代替发行核对。

## Dashboard 与文档站点

更新各自的 `package.json` 和 `pnpm-lock.yaml`，从仓库根目录执行构建：

```sh
bash dashboard/jt-dashboard-server-ui-vue3/build.sh
pnpm --dir docs docs:build
```

两端都使用普通的 Vite／VuePress 构建。第三方署名和适用条款分别集中在
[Dashboard NOTICE](../dashboard/jt-dashboard-server-ui-vue3/public/NOTICE.txt) 和
[文档站点 NOTICE](src/.vuepress/public/NOTICE.txt)，由构建工具复制到输出目录。
相同 MIT 条款共用一份，
保留各来源的版权；Lodash 的补充条款、BSD、Apache-2.0 和图标的 CC BY 4.0
按适用范围保留。不维护逐包版本清单，不自建许可扫描器或构建门禁。

Dashboard 的 `build.sh` 附带本模块 `LICENSE`，再将整个 `dist/` 同步到后端
静态目录；文档站点从 `public/` 复制 `LICENSE.txt`、`Apache-2.0.txt` 和
`NOTICE.txt`，产物留在被忽略的 `.vuepress/dist/` 中。
提交 Dashboard 静态目录的实际变化。
独立示例目录中的 mpegts.js 是固定的静态播放器副本，其旁边的既有
`THIRD-PARTY-NOTICES.txt` 继续随播放器交付；替换播放器时一同更新适用声明。

引入、更换或升级浏览器依赖、复制模板、Worker、图标时，核对变更涉及的原始
声明。来源、版权或许可有变化的，更新对应 `public/NOTICE.txt`；仅版本变化
且声明不变的，不需要修改许可材料。保留上游源码头和打包器保留的许可注释。
纯构建依赖不进入浏览器产物时，不为它们扩充站点声明。

前端已知缺项集中保留在 Dashboard 的 [NOTICE](../dashboard/jt-dashboard-server-ui-vue3/public/NOTICE.txt)：
element-plus-vite-starter 和 webworkify-webpack 的已核查材料声明 MIT，
但未附原始版权行或独立 LICENSE。补充说明不创造缺失的版权或新的授权。
此前核查的 lodash-unified 1.0.3 也存在同类缺项；当前浏览器产物只保留其实际
使用的 lodash-es，不再为已消除的 re-export 模块复制补充文件。Vue Devtools
等发布包缺项也合并到站点 NOTICE，不另存许可副本。来源或打包方式变化时复核。

## 复制源码和静态资源

保留文件头、原作者、许可和修改说明。复制后端源码时，在根目录 `NOTICE`
中按组件说明来源和适配用途。前端源码和静态资源的声明按上节路径维护。
各声明文件内可以共用相同的 MIT 条款，后端 Apache-2.0 代码复用项目 LICENSE。
`licenses/` 保留共用的 MPL／LGPL 全文和
jump3r 的源码交付说明及工具。外部依赖升级无需扩充源码逐文件记录。

Checkstyle 改编配置保留原始署名、LGPL 声明和修改说明，实际可编辑源码就是
`build-script/checkstyle/checkstyle.xml`，不另存一份上游配置作比较档案。

本地图标按[图标说明](src/.vuepress/styles/icons/README.md)保留许可和署名。
站点继续移除主题内嵌的 Crimson 字体子集，使用系统字体；新增要分发的字体时
保留该字体自己的许可。项目 JT 图形的生成方式见
[品牌资产](../assets/brand/README.md)。

历史调查和当时的清单可从 Git 历史追溯，不另维护归档副本或更新其中的文件哈希。
旧实现和旧资源的替换不追溯改变历史版本的授权状态。
历史贡献确认、正式发行范围和 jump3r 未决事项分别维护在现有专门文档，
不因简化而视为已解决。

日常验证沿用模块构建和前端构建，不另设许可打包校验任务。正式发行的授权范围以
[贡献确认](license-consents.md)和[发行清单](license-release-checklist.md)为准。
