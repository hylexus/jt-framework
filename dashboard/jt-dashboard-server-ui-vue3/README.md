# element-plus-vite-starter

## jt-framework 中的构建方式

本模块的已审核依赖使用 pnpm 8.15.9，在 `package.json` 的
`packageManager` 中固定。使用 Node.js 20 提供的 Corepack 执行构建，
避免全局 pnpm 的版本差异自动迁移锁文件或升级依赖：

```bash
bash build.sh
```

脚本会冻结安装依赖、构建 UI，再将完整产物（包括许可文件和
`license-build-provenance.json`）复制到后端的 `static/client/`。
本模块按独立项目安装，`--ignore-workspace` 用于保持原有锁文件结构；
构建不会读取 `pnpm-workspace.yaml` 中的 workspace 设置。

仅构建前端、不复制到后端时，在本目录执行：

```bash
corepack pnpm --ignore-workspace install --frozen-lockfile
corepack pnpm --ignore-workspace run build
```

升级依赖应作为单独变更进行，同时重新核对
`licenses/frontend-inventory.json` 和第三方声明。普通构建不更新锁文件，
也不自动接受新的许可审核记录。

依赖升级后的清单更新、声明生成和产物检查见
[依赖与许可维护清单](../../docs/license-maintenance.md#dashboard示例播放器依赖)。

> A starter kit for Element Plus with Vite

- Preview: <https://vite-starter.element-plus.org>

This is an example of on-demand element-plus with [unplugin-vue-components](https://github.com/antfu/unplugin-vue-components).

> If you want to import all, it may be so simple that no examples are needed. Just follow [quickstart | Docs](https://element-plus.org/zh-CN/guide/quickstart.html) and import them.

If you just want an on-demand import example `manually`, you can check [unplugin-element-plus/examples/vite](https://github.com/element-plus/unplugin-element-plus/tree/main/examples/vite).

## Project setup

```bash
npm install
```

### Compiles and hot-reloads for development

```bash
npm run dev
```

### Compiles and minifies for production

```bash
npm run build
```

## Usage

```bash
git clone https://github.com/element-plus/element-plus-vite-starter
cd element-plus-vite-starter
npm i
npm run dev
```

### Custom theme

See `src/styles/element/index.scss`.
