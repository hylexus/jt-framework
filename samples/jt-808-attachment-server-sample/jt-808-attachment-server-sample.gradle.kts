plugins {
    id("org.springframework.boot")
    application
}
apply(plugin = "org.springframework.boot")


application {
    mainClass.set("io.github.hylexus.jt.jt808.samples.attachment.boot2.Jt808AttachmentServerSampleApplication")
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}
apply<io.github.hylexus.jt.gradle.plugins.JtFrameworkFastModePlugin>()

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-808-server-spring-boot-starter-boot2"))
}
