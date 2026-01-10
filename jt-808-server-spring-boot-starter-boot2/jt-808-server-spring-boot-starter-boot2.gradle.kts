dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    api(project(":jt-808-server-spring-boot-autoconfigure"))
}

tasks.compileJava {
    dependsOn("processResources")
}

