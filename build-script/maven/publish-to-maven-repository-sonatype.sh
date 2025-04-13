#!/usr/bin/env sh

JT_PROJECT_PROJECT_ROOT_DIR=$(cd "$(dirname "$0")/../.."; pwd)
echo "JT_PROJECT_PROJECT_ROOT_DIR : ${JT_PROJECT_PROJECT_ROOT_DIR}"

cd ${JT_PROJECT_PROJECT_ROOT_DIR}
echo "Working-Directory           : ${JT_PROJECT_PROJECT_ROOT_DIR}"

# org.gradle.parallel=false @see https://stackoverflow.com/questions/72664149/gradle-maven-publish-sonatype-creates-multiple-repositories-that-cant-be-clos

./gradlew clean build publishMavenPublicationToSonatypeRepository -D org.gradle.parallel=false
