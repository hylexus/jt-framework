dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${JtFrameworkVersions.SPRING_BOOT_2_BOM_VERSION}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${JtFrameworkVersions.SPRING_BOOT_2_CLOUD_BOM_VERSION}")
    }
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    api(project(":jt-808-server-spring-boot-starter-boot2"))
    api(project(":jt-808-server-xtream-codec-adapter"))

    api("org.springframework.boot:spring-boot-starter")
}

tasks.compileJava {
    dependsOn("processResources")
}

