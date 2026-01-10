plugins {
    id("org.springframework.boot") version JtFrameworkVersions.SPRING_BOOT_3_BOM_VERSION
    application
}
apply(plugin = "org.springframework.boot")

springBoot {
    mainClass.set("io.github.hylexus.jt.demos.dashboard.webflux.boot3.JtDemoDashboardWebfluxBoot3")
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}
apply<io.github.hylexus.jt.gradle.plugins.JtFrameworkFastModePlugin>()

dependencies {

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":dashboard:jt-dashboard-server-spring-boot-starter"))

    api("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
