# 前端组件 MIT 许可与来源材料

材料核对日期：2026-10-17。

以下三个前端组件均在已核对的上游材料中明确声明采用 MIT 许可。
本项目按上游公开的 MIT 声明使用相应组件，并随前端产物保留这些声明、
适用署名和完整 MIT 条款。本目录保存对应的来源材料与声明生成依据。

| 组件 | 上游 MIT 声明位置 |
| --- | --- |
| lodash-unified 1.0.3 | 经过完整性校验的 npm 发布包中的 `package.json` |
| webworkify-webpack 2.1.5 | npm 发布包及固定提交中的 `package.json` 和 README；内嵌 webpack 的 MIT 声明另行保留 |
| element-plus-vite-starter | 固定对照提交中的 `package.json` |

目录中的文件用途如下：

- `*.package.json`：保留上游原始文件，其中 `license: MIT` 是组件的
  许可声明。作者字段按原文保留，不据此填写版权人或版权年份。
- `*-NOTICE.txt`：本项目整理的补充说明，包含组件来源、上游 MIT 声明、
  作者字段（上游存在时）及标准 MIT 条款。starter 的对应文件位于
  [element-plus-vite-starter-NOTICE.txt](../element-plus-vite-starter-NOTICE.txt)。
- [MIT.spdx.json](MIT.spdx.json) 和 [MIT-template.txt](MIT-template.txt)：
  来自 SPDX license-list-data v3.27.0 的 MIT 标准文本及模板。
  模板中的年份和版权人是占位符，补充说明中省略该占位行。
- [webworkify-webpack-2.1.5.license-excerpt.txt](webworkify-webpack-2.1.5.license-excerpt.txt)：
  从上游 README 提取的 MIT 声明原文，已合并到对应补充说明。
- [provenance.json](provenance.json)：记录精确版本、固定提交、来源链接、
  文件校验值及未找到的原始材料。`additionalReview` 保存详细核对结果，
  范围限于所列发布包、README 和模板快照，不覆盖上游仓库的完整历史。

组件的许可依据是上游 MIT 声明。本项目整理的补充说明用于随产物提供
组件来源、已有声明及标准 MIT 条款；补充文本不作为上游独立 LICENSE
原文，也不表示缺失的原始版权声明已找到。其中的 publisher author 字段
只是包元数据，不据此认定版权人；补充文本不创建新的版权声明或许可授予。
上游已附的 LICENSE 或 NOTICE 仍保留全文。取得新的适用原文后，应保留
其声明并更新分发材料；在最终发行前应重新核对这三个组件的原始署名材料。

已核对的发布包和模板快照未附独立 LICENSE 或原始版权行；原始材料
的收集范围及缺项保留在 `provenance.json`。其中也记录了 lodash-unified
发布元数据未提供仓库地址或 gitHead，以及 starter 的准确历史导入提交
未记录的情况。

生成前端声明时，脚本会校验发布包完整性、包内 MIT 声明及补充说明的
SHA-256，并将相应条款合并到根目录的 `THIRD-PARTY-FRONTEND-NOTICES.txt`
和前端产物的 `THIRD-PARTY-NOTICES.txt`。本目录中的材料还会随 Java
构件放入 `META-INF/licenses/frontend-mit/`。

后续维护：组件版本、复制来源或打包方式变化时，核对并更新相关材料。
若后续取得上游补充的版权声明、LICENSE 或 NOTICE，将其原文和固定
来源纳入 `provenance.json`、`../frontend-inventory.json` 和
`../source-audit.json` 的相关记录，再重新生成声明及前端产物。
出现相反的许可信息或权利主张时，重新核对受影响组件。
