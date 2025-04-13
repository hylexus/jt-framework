dependencies {
    // common start
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // common end

    compileOnly(project(":jt-1078-server-support"))

    compileOnly("jakarta.validation:jakarta.validation-api")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
}
