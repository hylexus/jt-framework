plugins {
    id("org.springframework.boot") version JtFrameworkVersions.SPRING_BOOT_3_BOM_VERSION
    application
}
apply(plugin = "org.springframework.boot")

springBoot {
    mainClass.set("io.github.hylexus.jt.demos.jt1078.mvc.JtDemo1078ServerWebMvcBoot3")
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}
apply<io.github.hylexus.jt.gradle.plugins.JtFrameworkFastModePlugin>()

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-1078-server-spring-boot-starter"))
    api(project(":dashboard:jt-dashboard-client-spring-boot-starter"))

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-websocket")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
