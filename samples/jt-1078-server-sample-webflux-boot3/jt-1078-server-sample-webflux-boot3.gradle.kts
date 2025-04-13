plugins {
    id("org.springframework.boot") version JtFrameworkVersions.MAXIMUM_SPRING_BOOT_BOM_VERSION
    application
}
apply(plugin = "org.springframework.boot")

springBoot {
    mainClass.set("io.github.hylexus.jt.jt1078.samples.webflux.boot3.Jt1078ServerSampleWebFluxBoot3Application")
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}
apply<io.github.hylexus.jt.gradle.plugins.JtFrameworkFastModePlugin>()

dependencies {
    // common start
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // common end

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-1078-server-spring-boot-starter"))
    api(project(":samples:jt-sample-common"))
    api("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    exclude("**/ClientTest.class")

    useJUnitPlatform()
    // https://github.com/gradle/gradle/issues/7773
    systemProperties(System.getProperties().map { (k, v) -> k.toString() to v }.toMap())
}
