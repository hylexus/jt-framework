#!/bin/bash
set -euo pipefail

JT_UI_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$JT_UI_DIR"

# Corepack uses package.json's pinned pnpm version. Keep this standalone
# project's lockfile unchanged, even when a workspace file exists.
corepack pnpm --ignore-workspace install --frozen-lockfile
corepack pnpm --ignore-workspace run build
cp LICENSE dist/LICENSE.txt

JT_UI_TARGET_DIR="$JT_UI_DIR/../jt-dashboard-server/src/main/resources/static/client"
rm -rf "$JT_UI_TARGET_DIR"
mkdir -p "$JT_UI_TARGET_DIR"
cp -R dist/. "$JT_UI_TARGET_DIR/"
