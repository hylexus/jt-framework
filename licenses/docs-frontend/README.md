# 文档站点第三方许可补充材料

本目录保存文档站点部分第三方组件的原始许可文本，用于补充依赖包中
未完整附带的许可或版权声明。文本保留上游原文及原作者署名。

- [vue-devtools-6.6.1-LICENSE.txt](vue-devtools-6.6.1-LICENSE.txt)：
  `@vue/devtools-api@6.6.1` 的 npm 发布包未附 LICENSE，因此保留
  [对应版本标签中的原始 LICENSE](https://raw.githubusercontent.com/vuejs/devtools-v6/v6.6.1/LICENSE)。
- [nprogress-0.2.0-LICENSE.txt](nprogress-0.2.0-LICENSE.txt)：来自
  [nprogress 0.2.0 发布归档](https://registry.npmjs.org/nprogress/-/nprogress-0.2.0.tgz)
  的 `package/License.md`。站点使用的
  `@vuepress/plugin-nprogress@2.0.0-rc.21` 包含源自 NProgress 的代码，
  其 `lib/client/nprogress.js` 保留了原作者 Rico Sta. Cruz 的版权头。
  插件根 LICENSE 仅列出 VuePress 的署名，因此另行保留 NProgress 的
  原始版权声明和完整 MIT 条款。文件名中的 `0.2.0` 标识许可文本来源。

搜索插件 Search Pro 的发布包还包含已打包的 Worker 及源码映射，
其中嵌入了 `slimsearch` 和 `@vuepress/helper` 的代码。依赖清单记录
Worker、源码映射和这两项源码的哈希；站点构建会核对它们与
`slimsearch@2.1.1`、`@vuepress/helper@2.0.0-rc.21` 对应源码的一致性。
两者的原始 LICENSE 由生成器直接从经过完整性校验的 npm 发布包提取。

[文档站点依赖清单](../docs-frontend-inventory.json)记录组件版本、发布包
完整性值，以及本目录许可文本的来源和 SHA-256。生成器将上述许可文本
合并到 `docs/src/.vuepress/public/THIRD-PARTY-NOTICES.txt`，随构建后的
文档站点一起提供。
