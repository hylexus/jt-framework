#!/bin/bash

# 确保脚本抛出遇到的错误
set -e

# 生成静态文件
pnpm run docs:build

# 进入生成的文件夹
cd src/.vuepress/dist
#
git init
git add -A
git commit -m 'deploy docs'
#
## 如果发布到 https://<USERNAME>.github.io/<REPO>
git push -f git@github.com:hylexus/jt-framework.git main:gh-pages
git push -f git@gitee.com:hylexus/jt-framework.git main:gh-pages
#
cd -
