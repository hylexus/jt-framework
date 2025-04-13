plugins {
    id("org.springframework.boot")
    application
}
apply(plugin = "org.springframework.boot")
springBoot {
    mainClass.set("io.github.hylexus.jt808.samples.customized.Jt808ServerSampleCustomizedApplication")
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

    api(project(":jt-808-server-spring-boot-starter-boot2"))
}
