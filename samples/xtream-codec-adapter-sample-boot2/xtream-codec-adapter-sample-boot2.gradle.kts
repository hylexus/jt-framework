plugins {
    id("org.springframework.boot")
    application
}
apply(plugin = "org.springframework.boot")

springBoot {
    mainClass.set("io.github.hylexus.jt.jt808.samples.xtreamcodec.boot2.XtreamCodecSampleBoot2Application")
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}
apply<io.github.hylexus.jt.gradle.plugins.JtFrameworkFastModePlugin>()

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${JtFrameworkVersions.SPRING_BOOT_2_BOM_VERSION}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${JtFrameworkVersions.SPRING_BOOT_2_CLOUD_BOM_VERSION}")
    }
}
dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-808-server-xtream-codec-adapter-spring-boot-starter-boot2"))

    api("org.springframework.boot:spring-boot-starter")
}

tasks.test {
    useJUnitPlatform()
    // https://github.com/gradle/gradle/issues/7773
    systemProperties(System.getProperties().map { (k, v) -> k.toString() to v }.toMap())
}
