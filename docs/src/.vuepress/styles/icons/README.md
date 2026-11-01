# 文档站点本地图标

本目录的 36 个实心 SVG 来自 **Font Awesome Free 6.7.2**，
Copyright 2024 Fonticons, Inc.，图标采用 **CC BY 4.0**。
原始 [LICENSE.txt](LICENSE.txt) 和 [CC BY 4.0 全文](CC-BY-4.0.txt)
保留在本目录，生成文档第三方声明时会一并附带。

上游完整 LICENSE 还介绍字体和代码的许可；这里仅使用 SVG 图标，
不分发 Font Awesome 字体或运行库。SVG 原始内容及其署名注释未修改，
只按现有项目映射重命名，通过 CSS mask 使用当前文字颜色。

固定的 npm 发布归档、完整性值、原始文件路径和每个本地文件的 SHA-256
记在[站点依赖清单](../../../../../licenses/docs-frontend-inventory.json)的
`localAssets` 中。生成声明及正常站点构建会检查图标和许可文件哈希；
更新图标时应从核对过的归档提取，并同步这些记录。

`../local-icons.scss` 保留现有的 45 个导航图标名称。例如：

| 名称／用途 | 上游实心图标 |
| --- | --- |
| launch：入门、快速开始 | rocket |
| api：基础 | plug |
| tool／build：辅助工具 | screwdriver-wrench |
| semantic：注解驱动开发 | laptop-code |
| extend：扩展、适配器 | diagram-project |
| config／customize／setting：配置、定制化 | gears |
| proposal：请先读我 | file-lines |
| merge：升级指南 | code-merge |

`vue` 和 `bitbucket` 继续使用通用代码、数据库图形，不使用品牌商标图形。
项目编写的 CSS 映射继续采用文档目录的 [MIT 许可](../../../../LICENSE)，
第三方 SVG 保留 CC BY 4.0，不因目录默认许可而改为 MIT。

主题使用空的 `iconAssets` 和本地 `jt-icon-` 前缀；SVG 内联进生产 CSS，
不请求远程 Iconfont CSS、WOFF、WOFF2 或 TTF。
