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

    api("org.springframework.boot:spring-boot-starter")
    api(project(":jt-core"))
    api(project(":dashboard:jt-dashboard-client"))

    compileOnly(project(":jt-808-server-support"))
    compileOnly(project(":jt-1078-server-support"))
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-starter-webflux")
    compileOnly("org.springframework.boot:spring-boot-starter-websocket")
}
