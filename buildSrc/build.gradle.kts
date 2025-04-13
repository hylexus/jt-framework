plugins {
    `kotlin-dsl`
}
repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        name = "aliyunGradlePlugin"
    }
    gradlePluginPortal()
    mavenCentral()
}


