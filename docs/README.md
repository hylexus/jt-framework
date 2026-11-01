# 文档与站点许可

本目录中维护者有权授权的原创文档和文档站点代码采用 [MIT](LICENSE)。
其他作者的历史贡献按原有许可和权利人的明确授权范围处理；本目录的
许可证声明不扩大未确认贡献的授权范围。已登记的 MIT 确认及仍待确认
的范围见[授权记录](license-consents.md)。

以下内容保留各自的许可：

- `example-src/` 中的 Java 示例继续采用 [Apache-2.0](example-src/LICENSE)。
  从后端源码引用到文档中的代码也保留其来源许可。
- 本项目自行绘制的 JT 标识及其衍生图形（logo、favicon、hero）采用
  Apache-2.0，源文件和生成记录见[品牌资产](../assets/brand/README.md)。
- 第三方组件、复制内容和引用资料保留原作者及原许可；已识别的站点
  源码声明见 [第三方说明](src/.vuepress/public/THIRD-PARTY-SOURCE-NOTICES.txt)。
- 浏览器 JS/CSS 和搜索 Worker 的依赖许可见
  [依赖声明](src/.vuepress/public/THIRD-PARTY-NOTICES.txt)，对应精确版本、
  归档完整性和 Worker 来源记录见
  [站点依赖清单](../licenses/docs-frontend-inventory.json)。
- 导航图标使用 Font Awesome Free 6.7.2 的
  [本地 SVG](src/.vuepress/styles/icons/README.md)，保留 CC BY 4.0 许可、
  原始署名和固定来源，不加载远程 Iconfont 字体。
- 文档文字使用系统字体回退。构建时移除主题内嵌的 Crimson 字体子集，
  不向浏览器分发这份字体；来源及处理见[资源说明](license-source-audit.md)。
  若依赖引入其他 `@font-face`，构建会报错，需先核对字体来源与许可。

文档站点输出附带 `LICENSE.txt`（MIT）、`Apache-2.0.txt`、`NOTICE.txt`、
两份第三方说明和 `license-build-provenance.json`。
依赖或引用内容不会因为本目录的 MIT 声明而改变许可。

在 `docs/` 下运行 `pnpm docs:license:generate` 可重新生成依赖声明，
`pnpm docs:license:check` 校验生成结果；需要 Python 3.10 或更新版本。
首次生成会下载锁定版本的 npm 归档并校验完整性，之后使用根目录
`build/frontend-license-cache/` 缓存。补充原始材料的出处见
[材料说明](../licenses/docs-frontend/README.md)。

正常 `pnpm docs:build` 会核对锁文件、实际进入浏览器产物的包版本、
已审核搜索 Worker 的来源、本地图标与许可文件的哈希及声明文件哈希，
并在站点输出中记录这些信息。
更新依赖时先以 `JT_DOCS_LICENSE_DISCOVER=1 pnpm docs:build` 收集
`build/docs-license-materials/browser-packages.json`，人工核对后更新依赖
清单与原始材料，重新生成声明，再运行正常构建。该环境变量仅用于
调查依赖，生成的临时站点不能用作已验证的交付产物。

依赖、第三方源码及静态资源变更的完整流程见[依赖与许可维护清单](license-maintenance.md)。
