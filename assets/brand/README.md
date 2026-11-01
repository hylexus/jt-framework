# jt-framework 图形标识

2026-10-03 为本项目重新绘制。深蓝圆角方形中，蓝色 J 与青绿色 T
分别代表项目名称。所有字形和轮廓直接由几何路径构成，没有采用外部
图标、字体字形或旧 logo 的路径。

- `jt-framework.svg`：可编辑的矢量文件，512 × 512 viewBox。
- `jt-framework-{size}.png`：16、32、48、64、128、256、512 px 透明 PNG。
- `manifest.json`：生成器和所有分发文件的 SHA-256，记录输出尺寸和配色。

图形材料按项目的 Apache-2.0 条款提供，参见根目录 `LICENSE`。
本目录的设计取代来源和许可尚未确认的旧立方体图标，以及此前使用的
VuePress logo／hero。VuePress 图片的已知 MIT 来源及历史记录见
[源码与资源说明](../../docs/license-source-audit.md)。

修改生成脚本中的路径或配色后，运行：

```sh
python3 build-script/generate-brand-assets.py
```

生成脚本使用 Python 标准库和 Cairo 动态库，会先查询系统中的 Cairo；
macOS 上也会尝试 Homebrew 的常见安装路径。运行会同步 Dashboard、文档
和 `.idea/icon.png` 的 SVG/PNG/ICO。ICO 包含 16、32、48、64、128、256 px。
SVG 也可在矢量编辑器中打开；若手动编辑，应同步更新生成脚本中的几何
路径，以免下一次生成覆盖修改。

更新 Dashboard 图标后，还需运行
`dashboard/jt-dashboard-server-ui-vue3/build.sh`，重新生成前端产物并同步到
server 静态目录。构建会更新 `license-build-provenance.json` 中 favicon
对应的哈希。其他第三方源码、依赖和模板继续保留它们自己的许可。
