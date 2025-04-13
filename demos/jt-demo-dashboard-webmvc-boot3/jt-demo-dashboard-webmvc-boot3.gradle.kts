plugins {
    id("org.springframework.boot") version JtFrameworkVersions.MAXIMUM_SPRING_BOOT_BOM_VERSION
    application
}
apply(plugin = "org.springframework.boot")

springBoot {
    mainClass.set("io.github.hylexus.jt.demos.dashboard.webmvc.boot3.JtDemoDashboardWebmvcBoot3")
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

    api(project(":dashboard:jt-dashboard-server-spring-boot-starter"))

    api("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
