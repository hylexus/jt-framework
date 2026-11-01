# 依赖与许可维护清单

按变更类型执行对应流程。普通业务代码或页面修改未影响依赖、第三方来源和
已登记资源时，按常规测试、构建验证即可。下列命令除特别注明外，在仓库根目录执行。

## 先判断需要维护什么

| 变更                      | 人工核对和更新                                              | 自动生成的内容                              |
|---------------------------|-------------------------------------------------------------|---------------------------------------------|
| JVM 依赖或使用范围        | 依赖配置、原始许可，必要时更新现有组件说明                  | Gradle 依赖报告、JAR 内的声明与适用源码材料 |
| Dashboard／示例播放器依赖 | `licenses/frontend-inventory.json`、必要的上游许可材料      | 根前端声明、示例声明、Dashboard 构建记录    |
| 文档站点依赖              | `licenses/docs-frontend-inventory.json`、必要的上游许可材料 | 站点第三方声明及构建记录                    |
| 复制或改编第三方源码      | 文件头、源码声明、来源链接及核查记录                        | 构建时复制进 JAR；来源记录本身不自动更新    |
| 图标、字体或项目图形      | 来源、许可、适用清单和资源哈希                              | 按对应资源流程生成声明或图形产物            |

## 后端 JVM 依赖

1. 修改依赖配置，确认直接及传递依赖的变化，特别是 `compileOnly` 改为运行时、
   复制源码或捆绑进应用等使用方式变化。
2. 生成报告，核对变更组件的 POM、归档内 LICENSE／NOTICE；对 Unknown 和多重
   许可结合实际文件判断。报告不覆盖测试专用依赖、构建插件及已复制的源码，
   这些内容变化时另查其原始材料。

   ```sh
   ./gradlew :generateLicenseReport
   ```

3. 若变化影响已登记的 jump3r、Logback 等组件，更新对应的 `licenses/` 材料和
   [依赖说明](license-jvm-review.md)；项目级分发说明变化时同步 `NOTICE`。
4. 运行相关测试、构建和 JAR 材料检查：

   ```sh
   ./gradlew build verifyJarLicenseMaterials
   ```

模块的 JVM `THIRD-PARTY-NOTICES.txt` 由 Gradle 自动生成到 `build/reports/`，
随 JAR 打包，不需要手工编辑或提交报告。普通 JVM 依赖升级通常不修改根目录的
`THIRD-PARTY-FRONTEND-NOTICES.txt` 或 `THIRD-PARTY-SOURCE-NOTICES.txt`。

交付包含外部库的 Boot 应用时，还需执行该模块的 `prepareDistributionLicenses`，
核对生成的配套材料并随应用提供；普通 `build` 不自动执行此任务。
命令示例及适用源码、替换步骤见[分发说明](license-jvm-review.md#普通库与完整应用分别处理)。

## Dashboard／示例播放器依赖

1. 使用 Dashboard `package.json` 固定的 pnpm 版本更新依赖及锁文件，操作方式见
   [前端构建说明](../dashboard/jt-dashboard-server-ui-vue3/README.md)。
2. 人工审核并更新 `licenses/frontend-inventory.json`：
   - `packages`：新增、升级、移除的直接及传递依赖；`name`、`version` 和
     `integrity` 对应锁文件中的包及 `resolution.integrity`。
   - 清单还覆盖构建时使用的图标数据和示例播放器；只列 `package.json` 中的
     直接依赖不足以覆盖这些内容。
   - 随版本复核 `licenseSource`、`additionalLicenseSources`、`licenseSupplement`
     及 `copiedSources` 中的适用原文、来源和哈希。未变化的复制来源保留原记录。
   - 审核完成后，用下列结果更新 `lockfileSha256`：

     ```sh
     shasum -a 256 dashboard/jt-dashboard-server-ui-vue3/pnpm-lock.yaml
     ```

3. 若升级示例播放器，同步更新示例静态目录中的实际 JS 文件；生成器会校验它与
   对应发布包的一致性。播放器新增内嵌组件时，一并更新清单及生成器的声明范围。
4. 生成声明、构建并同步 Dashboard，再检查产物：

   ```sh
   python3 build-script/generate-frontend-license-notices.py
   bash dashboard/jt-dashboard-server-ui-vue3/build.sh
   python3 build-script/generate-frontend-license-notices.py --check
   python3 build-script/check-license-materials.py dashboard dashboard/jt-dashboard-server-ui-vue3/dist
   ```

生成器更新根目录 `THIRD-PARTY-FRONTEND-NOTICES.txt` 和三个示例目录的
`THIRD-PARTY-NOTICES.txt`。`build.sh` 将完整 UI 产物同步到后端的
`src/main/resources/static/client/`。该目录中已纳入仓库的资源、许可文件及
`license-build-provenance.json` 随变更一起提交；构建记录由 Vite 自动生成。

## 文档站点依赖

1. 更新 `docs/package.json` 及其锁文件，保持文档项目使用的 pnpm 锁文件格式。
2. 在 `docs/` 下执行发现构建，收集实际进入浏览器的依赖：

   ```sh
   JT_DOCS_LICENSE_DISCOVER=1 pnpm docs:build
   ```

3. 根据根目录 `build/docs-license-materials/browser-packages.json` 和锁文件，
   审核并更新 `licenses/docs-frontend-inventory.json` 的 `packages`，包含
   包名、版本、完整性值和必要的补充许可来源；同时更新锁文件哈希：

   ```sh
   shasum -a 256 docs/pnpm-lock.yaml
   ```

   搜索 Worker、图标或字体变化时，一并核对 `prebuiltAssets`、`localAssets`
   及其原始材料。发现模式仍执行资源校验；遇到这些校验报错时先核对对应材料。
4. 回到仓库根目录，生成声明并执行正常构建：

   ```sh
   python3 build-script/generate-docs-license-notices.py
   python3 build-script/generate-docs-license-notices.py --check
   pnpm --dir docs run docs:build
   ```

生成器自动更新 `docs/src/.vuepress/public/THIRD-PARTY-NOTICES.txt` 和清单的
`noticesSha256`。
提交更新的清单、声明及适用的原始材料；站点 `dist/` 和发现构建记录留在忽略目录。
清单与锁文件哈希需人工审核后更新，生成器不会自动接受新的依赖集合。

## 第三方源码与静态资源

- **复制／改编源码**：保留原始署名、许可和修改说明，更新根目录
  `THIRD-PARTY-SOURCE-NOTICES.txt` 及 `licenses/source-provenance.json` 中的固定来源。
  更新核查记录时，同步 `licenses/source-audit.json` 中受影响的当前文件哈希，
  保留历史记录。普通业务文件修改不要求重做整份来源核查。
- **源码声明副本**：根声明修改后，手动同步文档站点副本：

  ```sh
  cp THIRD-PARTY-SOURCE-NOTICES.txt docs/src/.vuepress/public/THIRD-PARTY-SOURCE-NOTICES.txt
  ```

- **复制的前端模板**：同时核对 `frontend-inventory.json` 的 `copiedSources`，
  更新对应许可材料并重新生成前端声明。
- **导航图标**：按[本地图标说明](src/.vuepress/styles/icons/README.md)保留来源和
  适用许可，更新文档清单并重新生成声明、构建。
- **字体**：当前站点使用系统字体，构建会拒绝新增 `@font-face`。若改为分发字体，
  先核对字体来源和许可，再调整构建策略。
- **项目 JT 标识**：修改生成器后运行 `python3 build-script/generate-brand-assets.py`，
  同步来源核查记录中的当前哈希，再运行 Dashboard `build.sh` 更新静态资源与构建记录；
  详见[品牌说明](../assets/brand/README.md)。
- **项目声明或许可范围**：核对受影响目录的 LICENSE／NOTICE 及其分发副本。
  各目录的 MIT、Apache-2.0 和第三方许可分别保留。

## 提交与交付前核对

- [ ] 本次变更已包含源配置、锁文件、受影响的清单和必要的上游原始材料。
- [ ] 已重新生成声明；根源码声明与站点副本一致。
- [ ] 已按变更范围构建、检查，Dashboard 后端静态产物与前端构建一致。
- [ ] `build/` 报告、下载缓存、`node_modules/` 和忽略的 `dist/` 未纳入提交。
- [ ] 若交付完整应用，已核对并随附实际构件对应的许可／源码材料。

以上检查针对材料维护；正式发行的版本、授权范围及历史贡献确认以
[许可发行清单](license-release-checklist.md)为准。
