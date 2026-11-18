# 贡献指南 / Contributing

提交 PR 时，请说明改动目的和验证结果，并完成
[PR 模板](.github/PULL_REQUEST_TEMPLATE.md)中的贡献确认。

Describe the purpose of your PR and its validation results, and complete the
contribution confirmations in the [PR template](.github/PULL_REQUEST_TEMPLATE.md).

## 贡献与许可

请确认你有权提交贡献，并同意按所修改目录或文件适用的许可提供贡献。
版权仍归各自版权人所有。

Confirm that you have the right to submit your contribution and agree to provide
it under the license applicable to the modified directory or file.
Copyright remains with the respective rights holders.

新提交的项目原创贡献按下表提供；文件或目录另有适用许可时，遵循其声明：

New original project contributions use the licenses below; an applicable
file- or directory-specific license takes precedence.

| 内容 / Contribution                       | 许可 / License |
|--------------------------------------------|------------|
| Java 后端、文档 Java 示例、本项目自行绘制的 JT 标识及其衍生图形 / Java backend, Java documentation examples, original JT mark and derived artwork | Apache-2.0 |
| 文档、文档站点代码、Dashboard UI、示例前端 / Documentation, site code, Dashboard UI, sample frontends | MIT |

修改第三方文件时，继续遵守其原许可，保留版权、来源和修改说明。
引入或改编第三方源码时，请注明来源和版本／提交，附带适用许可，
并在相应的声明文件中按组件记录来源。后端使用根 [NOTICE](NOTICE)，
前端声明的位置见[许可维护](docs/license-maintenance.md#dashboard-与文档站点)。

Keep third-party files under their original licenses and preserve copyright,
source attribution and modification notices. When copying or adapting third-party
source, identify the source and version or commit, include the applicable license,
and record the attribution in the relevant notice. For backend code use the root
[NOTICE](NOTICE). For frontend notices see [license maintenance](docs/license-maintenance.md#dashboard-与文档站点).

历史贡献授权仍在确认；其适用范围以原有许可和权利人的明确确认为准。
上述约定仅适用于本次新提交的贡献，不代替历史贡献的授权确认。
当前范围与发行安排见[许可发行清单](docs/license-release-checklist.md)。

Historical contribution permissions are still being confirmed; existing licenses
and the rights holders' express grants determine their scope. These contribution
rules apply only to new submissions and do not replace historical consent.
See the [release checklist](docs/license-release-checklist.md) for scope and release plans.

## 验证与维护

请按改动范围运行测试和构建，参见 [README 构建说明](README.md#building-from-source--构建源码)。
依赖升级、第三方源码或静态资源变更时，按[许可维护](docs/license-maintenance.md)
执行对应流程；前端声明集中在各自的 `public/NOTICE.txt`，署名或许可变化时更新，
不另维护依赖版本清单或文件哈希。

Run tests and builds appropriate to the change; see [building from source](README.md#building-from-source--构建源码).
For dependency, copied-source or static-resource changes, follow [license maintenance](docs/license-maintenance.md).
Frontend notices are centralized in each project's `public/NOTICE.txt`; update them when attribution or licenses
change, without maintaining a separate dependency-version list or file hashes.
