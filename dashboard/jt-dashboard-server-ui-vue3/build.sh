#!/bin/bash
set -e
__JT_WORKING_DIR__=$(cd "$(dirname "$0")";pwd)
echo "Current Working Dir: ${__JT_WORKING_DIR__}"

cd ${__JT_WORKING_DIR__}

pnpm install
pnpm build

__TARGET_DIR__="../jt-dashboard-server/src/main/resources/static/client"
rm -rf ${__TARGET_DIR__}
mkdir -p ${__TARGET_DIR__}
cp -r dist/** ${__TARGET_DIR__}

cd -
