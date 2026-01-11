import io.github.hylexus.jt.gradle.utils.JtFrameworkConfig.jtFrameworkConfig

plugins {
    id("org.springframework.boot")
    application
}
apply(plugin = "org.springframework.boot")

springBoot {
    mainClass.set("io.github.hylexus.jt.jt808.samples.xtreamcodec.boot3.XtreamCodecSampleBoot3Application")
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}
apply<io.github.hylexus.jt.gradle.plugins.JtFrameworkFastModePlugin>()
dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${JtFrameworkVersions.SPRING_BOOT_3_BOM_VERSION}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${JtFrameworkVersions.SPRING_BOOT_3_CLOUD_BOM_VERSION}")
    }
}
dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-808-server-xtream-codec-adapter-spring-boot-starter"))
}

tasks.test {
    useJUnitPlatform()
    // https://github.com/gradle/gradle/issues/7773
    systemProperties(System.getProperties().map { (k, v) -> k.toString() to v }.toMap())
}
