dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    api(project(":jt-808-server-spring-boot-autoconfigure"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.compileJava {
    dependsOn("processResources")
}
