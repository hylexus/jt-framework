#!/usr/bin/env sh

JT_PROJECT_PROJECT_ROOT_DIR=$(cd "$(dirname "$0")/../.."; pwd)
echo "JT_PROJECT_PROJECT_ROOT_DIR : ${JT_PROJECT_PROJECT_ROOT_DIR}"

cd ${JT_PROJECT_PROJECT_ROOT_DIR}
echo "Working-Directory           : ${JT_PROJECT_PROJECT_ROOT_DIR}"

./gradlew clean build publishToMavenLocal
