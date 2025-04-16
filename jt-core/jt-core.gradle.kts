dependencies {
    // common start
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // common end

    compileOnly("org.springframework.boot:spring-boot-starter-logging")
    compileOnly("org.springframework:spring-webflux")
    compileOnly("org.springframework:spring-webmvc")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")

    api("io.netty:netty-buffer")
    api("io.netty:netty-transport")
    api("io.netty:netty-transport-classes-epoll")

    api("io.github.hylexus.oaks:oaks-common-utils") {
        exclude(group = "org.projectlombok", module = "lombok")
    }
    api("com.google.guava:guava") {
        exclude(group = "org.checkerframework", module = "checker-qual")
    }
    api("org.apache.commons:commons-lang3")
    api("org.bouncycastle:bcprov-jdk18on")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
tasks.test {
    exclude("**/CommandWaitingPoolTest.class")

    useJUnitPlatform()
    // https://github.com/gradle/gradle/issues/7773
    systemProperties(System.getProperties().map { (k, v) -> k.toString() to v }.toMap())
}
