dependencies {
    api(project(":jt-core"))
    api(project(":dashboard:jt-dashboard-common"))
    compileOnly(project(":jt-808-server-support"))
    compileOnly(project(":jt-1078-server-support"))
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-starter-webflux")
    compileOnly("org.springframework.boot:spring-boot-starter-websocket")
    api("org.springframework.boot:spring-boot-starter-actuator")
}
