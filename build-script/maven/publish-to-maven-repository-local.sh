#!/usr/bin/env sh

JT_PROJECT_PROJECT_ROOT_DIR=$(cd "$(dirname "$0")/../.."; pwd)
echo "JT_PROJECT_PROJECT_ROOT_DIR : ${JT_PROJECT_PROJECT_ROOT_DIR}"

cd ${JT_PROJECT_PROJECT_ROOT_DIR}
echo "Working-Directory           : ${JT_PROJECT_PROJECT_ROOT_DIR}"

# @see jt-framework.maven.repo.central-portal.artifacts.temp-dir
rm -rf /tmp/jt-framework/temp-artifacts

./gradlew clean build publishMavenPublicationToMavenLocal \
-P jt-framework.maven.repo.central-portal.enabled=false \
-P jt-framework.maven.repo.private.enabled=false \
-P jt-framework.maven.repo.github.enabled=false \
-P jt-framework.maven.publications.signing=off \
-P jt-framework.backend.build.checkstyle.enabled=true \
-P jt-framework.backend.build.debug-module-fatjar.enabled=false
