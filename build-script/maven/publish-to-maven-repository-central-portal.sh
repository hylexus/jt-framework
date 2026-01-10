#!/usr/bin/env sh

JT_PROJECT_PROJECT_ROOT_DIR=$(cd "$(dirname "$0")/../.."; pwd)
echo "JT_PROJECT_PROJECT_ROOT_DIR : ${JT_PROJECT_PROJECT_ROOT_DIR}"

cd ${JT_PROJECT_PROJECT_ROOT_DIR}
echo "Working-Directory           : ${JT_PROJECT_PROJECT_ROOT_DIR}"

# @see jt-framework.maven.repo.central-portal.artifacts.temp-dir
rm -rf /tmp/jt-framework/temp-artifacts

# org.gradle.parallel=false @see https://stackoverflow.com/questions/72664149/gradle-maven-publish-sonatype-creates-multiple-repositories-that-cant-be-clos
# https://central.sonatype.org/publish/publish-portal-api/

./gradlew clean build publishToMavenCentralPortal \
-P jt-framework.maven.repo.central-portal.enabled=true \
-P jt-framework.maven.repo.private.enabled=false \
-P jt-framework.maven.repo.github.enabled=false \
-P jt-framework.maven.publications.signing=on \
-P jt-framework.backend.build.checkstyle.enabled=true \
-P jt-framework.backend.build.debug-module-fatjar.enabled=false
