dependencies {
    // common start
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // common end

    api(project(":jt-core"))

    api("org.springframework:spring-context")

    api("org.apache.commons:commons-lang3")

    api("com.github.sarveswaran-m:util.concurrent.blockingMap")
    api("com.github.ben-manes.caffeine:caffeine") {
        exclude(group = "com.google.errorprone", module = "error_prone_annotations")
    }

    api("io.netty:netty-all")

    compileOnly("org.springframework.boot:spring-boot-starter-logging")
    compileOnly("org.springframework.boot:spring-boot-starter-data-redis")

    testCompileOnly("org.springframework.boot:spring-boot-starter-json")
    testRuntimeOnly("org.springframework.boot:spring-boot-starter-json")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("com.google.code.findbugs:jsr305")
    compileOnly("com.google.code.findbugs:annotations")

    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    // https://github.com/gradle/gradle/issues/7773
    systemProperties(System.getProperties().map { (k, v) -> k.toString() to v }.toMap())
}
