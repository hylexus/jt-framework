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

    api(project(":jt-808-server-support"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.hibernate:hibernate-validator")

    compileOnly("org.springframework.boot:spring-boot-starter-data-redis")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    compileOnly("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

}

tasks.compileJava {
    dependsOn("processResources")
}
