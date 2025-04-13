plugins {
    id("org.springframework.boot") version JtFrameworkVersions.MAXIMUM_SPRING_BOOT_BOM_VERSION
    application
}

apply(plugin = "org.springframework.boot")
application {
    mainClass.set("io.github.hylexus.jt808.samples.bare.Jt808ServerSampleBareApplication")
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}
apply<io.github.hylexus.jt.gradle.plugins.JtFrameworkFastModePlugin>()

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-808-server-spring-boot-starter"))
//    api("org.springframework.boot:spring-boot-starter:3.0.2")
}
