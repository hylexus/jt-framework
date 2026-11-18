# 文档与站点许可

本目录中维护者有权授权的原创文档和文档站点代码采用 [MIT](LICENSE)。
其他作者的历史贡献按原有许可和权利人的明确授权范围处理。
已登记的文档和站点 MIT 授权范围见[授权记录](license-consents.md)。

以下内容保留各自的许可：

- `example-src/` 中的 Java 示例继续采用 [Apache-2.0](example-src/LICENSE)。
  从后端源码引用到文档中的代码也保留其来源许可。
- 本项目自行绘制的 JT 标识及其衍生图形（logo、favicon、hero）采用
  Apache-2.0，源文件和生成记录见[品牌资产](../assets/brand/README.md)。
- 第三方组件、复制内容和引用资料保留原作者及原许可；已识别的站点
  源码与图标声明统一见站点 [NOTICE](src/.vuepress/public/NOTICE.txt)。
- 浏览器 JS/CSS 和搜索 Worker 的署名及适用条款集中在同一份站点
  `NOTICE.txt`，相同 MIT 条款共用一份，不维护逐包版本清单。
- 导航图标使用 Font Awesome Free 6.7.2 的
  [本地 SVG](src/.vuepress/styles/icons/README.md)，保留 CC BY 4.0 许可、
  原始署名和固定来源，不加载远程 Iconfont 字体。
- 文档文字使用系统字体回退。构建时移除主题内嵌的 Crimson 字体子集，
  不向浏览器分发这份字体；处理方式见[许可维护](license-maintenance.md)。

文档站点输出附带 `LICENSE.txt`（MIT）、`Apache-2.0.txt` 和 `NOTICE.txt`。
运行 `pnpm docs:build`，VuePress 会从 `public/` 原样复制这些材料，
不需要自定义许可生成脚本、扫描门禁或哈希更新。

依赖、搜索 Worker、本地图标或复制内容的来源、版权或许可变化时，更新
站点 [NOTICE](src/.vuepress/public/NOTICE.txt)。主题、内嵌 NProgress 和
Vue Devtools 的原始署名及 MIT 条款也集中在其中。
依赖或引用内容不会因为本目录的 MIT 声明而改变许可。

完整操作见 [许可维护](license-maintenance.md)。
