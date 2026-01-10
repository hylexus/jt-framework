dependencies {

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-1078-server-spring-boot-autoconfigure")) {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter")
    }

    api("org.springframework.boot:spring-boot-starter")

}

tasks.compileJava {
    dependsOn("processResources")
}
