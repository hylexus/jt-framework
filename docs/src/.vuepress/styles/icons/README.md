# 文档站点本地图标

本目录的 36 个实心 SVG 来自 **Font Awesome Free 6.7.2**，
Copyright 2024 Fonticons, Inc.，图标采用 **CC BY 4.0**。
原始 [LICENSE.txt](LICENSE.txt) 和 [CC BY 4.0 全文](CC-BY-4.0.txt)
保留在本目录；站点 `NOTICE.txt` 也附带图标署名和 CC BY 4.0 全文。

上游完整 LICENSE 还介绍字体和代码的许可；这里仅使用 SVG 图标，
不分发 Font Awesome 字体或运行库。SVG 原始内容及其署名注释未修改，
只按现有项目映射重命名，通过 CSS mask 使用当前文字颜色。

来源为 [Font Awesome Free 6.7.2 发布归档](https://registry.npmjs.org/@fortawesome/fontawesome-free/-/fontawesome-free-6.7.2.tgz)。
组件署名和使用方式统一保存在站点 [NOTICE.txt](../../public/NOTICE.txt)，
构建将这份集中声明原样复制到站点。更换图标来源或许可时更新这些材料，
无需维护每个 SVG 的哈希；原始逐文件核对可从 Git 历史追溯。

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
