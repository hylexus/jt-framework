dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-1078-server-support"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.hibernate:hibernate-validator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.compileJava {
    dependsOn("processResources")
}
