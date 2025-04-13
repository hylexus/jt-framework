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

    api("io.netty:netty-all")
    api("io.projectreactor:reactor-core")
    api("org.springframework:spring-context")
    api("org.apache.commons:commons-lang3")

    api("com.google.guava:guava") {
        exclude(group = "org.checkerframework", module = "checker-qual")
    }
    api("io.github.hylexus.oaks:oaks-common-utils") {
        exclude(group="org.projectlombok", module="lombok")
    }
    api("org.apache.commons:commons-collections4")
    api("com.github.ben-manes.caffeine:caffeine") {
        exclude(group = "com.google.errorprone", module = "error_prone_annotations")
    }

    // Pure Java MP3 Encoder. Easy-to-use java tool and Ideal solution for a built-in MP3 encoder library.
    // Supported source formats: - WAVE_FORMAT_PCM - WAVE_FORMAT_IEEE_FLOAT 32bit per sample only
    // https://sourceforge.net/projects/jump3r/
    // pcm ==> mp3
    api("de.sciss:jump3r:1.0.5")

    compileOnly("org.springframework.boot:spring-boot-starter-logging")
    compileOnly("com.google.code.findbugs:jsr305")
    compileOnly("com.google.code.findbugs:annotations")

    compileOnly("org.springframework.boot:spring-boot-starter-data-redis")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.test {
    useJUnitPlatform()
    // https://github.com/gradle/gradle/issues/7773
    systemProperties(System.getProperties().map { (k, v) -> k.toString() to v }.toMap())
}

