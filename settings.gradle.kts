pluginManagement {
    val defaultSpringBootBomVersion: String by settings
    plugins {
        // A Gradle plugin that provides Maven-like dependency management and exclusions
        // @see https://docs.spring.io/dependency-management-plugin/docs/1.1.7/reference/html/#getting-started
        id("io.spring.dependency-management") version "1.1.7" apply false
        id("org.springframework.boot") version defaultSpringBootBomVersion apply false
        id("io.gitee.pkmer.pkmerboot-central-publisher") version "1.1.1" apply false
    }
    repositories {
        listOf(
            "https://maven.aliyun.com/repository/public",
            "https://mirrors.cloud.tencent.com/nexus/repository/maven-public",
            "https://repo.huaweicloud.com/repository/maven",
//            "https://maven.aliyun.com/repository/gradle-plugin",
        ).forEach {
            maven {
                url = uri(it)
                name = it
                content {
                    // 上面几个镜像都没这个依赖
                    excludeGroup("net.minecraftforge.licenser")
                    // 排除腾讯云 404 的依赖
                    if (it.contains("tencent")) {
                        excludeGroup("com.github.jk1.gradle-license-report")
                    }
                }
            }
        }
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

}

// modules
include("jt-core")
include("jt-808-server-support")
include("jt-808-server-spring-boot-autoconfigure")
include("jt-808-server-spring-boot-starter-boot2")
include("jt-808-server-spring-boot-starter")
include("jt-808-server-xtream-codec-adapter")
include("jt-808-server-xtream-codec-adapter-spring-boot-starter")
include("jt-808-server-xtream-codec-adapter-spring-boot-starter-boot2")
include("jt-1078-server-support")
include("jt-1078-server-spring-boot-autoconfigure")
include("jt-1078-server-spring-boot-starter-boot2")
include("jt-1078-server-spring-boot-starter")
include("dashboard")
include("dashboard:jt-dashboard-common")
include("dashboard:jt-dashboard-server")
include("dashboard:jt-dashboard-server-spring-boot-starter")
include("dashboard:jt-dashboard-client")
include("dashboard:jt-dashboard-client-spring-boot-starter")

// samples
include("samples")
include("samples:jt-808-attachment-server-sample")
include("samples:jt-sample-common")
include("samples:jt-808-server-sample-debug")
include("samples:jt-808-server-sample-bare")
include("samples:jt-808-server-sample-bare-boot3")
include("samples:jt-808-server-sample-customized")
include("samples:jt-808-server-sample-annotation")
include("samples:jt-808-client-sample-debug")
include("samples:jt-1078-server-sample-webflux-boot3")
include("samples:jt-1078-server-sample-webmvc-boot3")
include("samples:xtream-codec-adapter-sample-boot2")
include("samples:xtream-codec-adapter-sample-boot3")

// demos
include("demos")
include("demos:jt-demo-808-server-webflux-boot3")
include("demos:jt-demo-808-server-webmvc-boot3")
include("demos:jt-demo-1078-server-webflux-boot3")
include("demos:jt-demo-1078-server-webmvc-boot3")
include("demos:jt-demo-dashboard-webflux-boot3")
include("demos:jt-demo-dashboard-webmvc-boot3")

rootProject.name = "jt-framework"

dependencyResolutionManagement {
    repositories {
        extraMavenRepositoryUrls().forEach {
            maven(it)
        }
        mavenCentral()
        mavenLocal()
    }
}

setBuildFileName(rootProject)

fun setBuildFileName(project: ProjectDescriptor) {
    project.children.forEach {
        it.buildFileName = "${it.name}.gradle.kts"
        assert(it.projectDir.isDirectory())
        assert(it.buildFile.isFile())
        setBuildFileName(it)
    }
}

fun extraMavenRepositoryUrls() = listOf(
    // "https://mirrors.cloud.tencent.com/nexus/repository/maven-public",
    // "https://repo.huaweicloud.com/repository/maven",
    "https://maven.aliyun.com/repository/central",
    "https://maven.aliyun.com/repository/public",
    "https://maven.aliyun.com/repository/google",
    "https://maven.aliyun.com/repository/spring",
    // Central
    "https://repo1.maven.org/maven2",
    "https://maven.aliyun.com/repository/spring-plugin",
    "https://maven.aliyun.com/repository/gradle-plugin",
    "https://maven.aliyun.com/repository/grails-core",
    "https://maven.aliyun.com/repository/apache-snapshots",
    "https://plugins.gradle.org/m2/",
    // "https://repo.spring.io/release",
    // "https://repo.spring.io/snapshot"
)
