import org.gradle.api.JavaVersion
import org.gradle.api.Project

object JtFrameworkVersions {
    const val SPRING_BOOT_2_BOM_VERSION = "2.7.18"
    const val SPRING_BOOT_3_BOM_VERSION = "3.3.0"
    const val SPRING_BOOT_2_CLOUD_BOM_VERSION = "2021.0.8"
    const val SPRING_BOOT_3_CLOUD_BOM_VERSION = "2023.0.2"

    val COMPATIBILITY_MATRIX = mapOf<String, CompatibilityDefinition>()
        .plus(
            listOf<String>(
                "jt-core",
                "jt-808-server-support",
                "jt-808-server-spring-boot-autoconfigure",
                "jt-808-server-spring-boot-starter-boot2",
                "jt-1078-server-support",
                "jt-1078-server-spring-boot-autoconfigure",
                "jt-1078-server-spring-boot-starter-boot2",
            ).map { name ->
                name to CompatibilityDefinition(
                    name = name,
                    jdkVersion = JavaVersion.VERSION_1_8,
                    springBootVersion = SPRING_BOOT_2_BOM_VERSION,
                    springCloudVersion = SPRING_BOOT_2_CLOUD_BOM_VERSION
                )
            }
        )
        .plus(
            listOf<String>(
                "jt-808-server-spring-boot-starter",
                "jt-1078-server-spring-boot-starter",
                "jt-dashboard-common",
                "jt-dashboard-server",
                "jt-dashboard-server-spring-boot-starter",
                "jt-dashboard-client",
                "jt-dashboard-client-spring-boot-starter",
            ).map { name ->
                name to CompatibilityDefinition(
                    name = name,
                    jdkVersion = JavaVersion.VERSION_17,
                    springBootVersion = SPRING_BOOT_3_BOM_VERSION,
                    springCloudVersion = SPRING_BOOT_3_CLOUD_BOM_VERSION
                )
            }
        )
        .plus(
            listOf<String>(
                "jt-808-attachment-server-sample",
                "jt-808-client-sample-debug",
                "jt-808-server-sample-annotation",
                "jt-808-server-sample-bare",
                "jt-808-server-sample-customized",
                "jt-808-server-sample-debug",
                "jt-sample-common",
            ).map { name ->
                name to CompatibilityDefinition(
                    name = name,
                    jdkVersion = JavaVersion.VERSION_1_8,
                    springBootVersion = SPRING_BOOT_2_BOM_VERSION,
                    springCloudVersion = SPRING_BOOT_2_CLOUD_BOM_VERSION
                )
            }
        )
        .plus(
            listOf<String>(
                "jt-808-server-sample-bare-boot3",
                "jt-1078-server-sample-webflux-boot3",
                "jt-1078-server-sample-webmvc-boot3",
                "jt-demo-808-server-webflux-boot3",
                "jt-demo-808-server-webmvc-boot3",
                "jt-demo-1078-server-webflux-boot3",
                "jt-demo-1078-server-webmvc-boot3",
                "jt-demo-dashboard-webflux-boot3",
                "jt-demo-dashboard-webmvc-boot3",
            ).map { name ->
                name to CompatibilityDefinition(
                    name = name,
                    jdkVersion = JavaVersion.VERSION_17,
                    springBootVersion = SPRING_BOOT_3_BOM_VERSION,
                    springCloudVersion = SPRING_BOOT_3_CLOUD_BOM_VERSION
                )
            }
        )

    val MODULES_TO_PUBLISH_TO_MAVEN_REPO = listOf<String>(
        "jt-core",
        "jt-808-server-support",
        "jt-808-server-spring-boot-autoconfigure",
        "jt-808-server-spring-boot-starter-boot2",
        "jt-808-server-spring-boot-starter",
    )
}

fun Project.compatibilityDefinition(): CompatibilityDefinition {
    return JtFrameworkVersions.COMPATIBILITY_MATRIX[name]
        ?: throw IllegalArgumentException("Project <<$name>> not found in JtFrameworkVersions.COMPATIBILITY_MATRIX")
}

fun Project.isMavenPublication(): Boolean {
    return JtFrameworkVersions.MODULES_TO_PUBLISH_TO_MAVEN_REPO.contains(project.name)
}

fun Project.isJavaProject(): Boolean {
    return JtFrameworkVersions.COMPATIBILITY_MATRIX.containsKey(project.name)
}

fun printCompatibilityMatrix() {
    val matrix = JtFrameworkVersions.COMPATIBILITY_MATRIX.values

    // 计算各列的最大内容长度
    val maxNameLength = matrix.maxOf { it.name.length }
    val maxJdkLength = matrix.maxOf { it.jdkVersion.majorVersion.length }
    val maxBootLength = matrix.maxOf { it.springBootVersion.length }
    val maxCloudLength = matrix.maxOf { it.springCloudVersion.length }

    // 每列最终宽度：取内容最大 + 标题长度的最大值
    val nameWidth = maxOf(maxNameLength, "Project".length)
    val jdkWidth = maxOf(maxJdkLength, "JDK".length)
    val bootWidth = maxOf(maxBootLength, "Spring Boot".length)
    val cloudWidth = maxOf(maxCloudLength, "Spring Cloud".length)

    // 加上左右边距（| 和空格）
    val format = "| %-${nameWidth}s | %-${jdkWidth}s | %-${bootWidth}s | %-${cloudWidth}s |"

    // 打印上边框
    println("+-${"-".repeat(nameWidth)}-+-${"-".repeat(jdkWidth)}-+-${"-".repeat(bootWidth)}-+-${"-".repeat(cloudWidth)}-+")

    // 打印标题行（注意：标题也要按宽度填充）
    println(format.format("Project", "JDK", "Spring Boot", "Spring Cloud"))

    // 打印中边框
    println("+-${"-".repeat(nameWidth)}-+-${"-".repeat(jdkWidth)}-+-${"-".repeat(bootWidth)}-+-${"-".repeat(cloudWidth)}-+")

    // 打印数据行
    matrix.forEach { entry ->
        println(
            format.format(
                entry.name,
                entry.jdkVersion.majorVersion,
                entry.springBootVersion,
                entry.springCloudVersion
            )
        )
    }

    // 打印底边框
    println("+-${"-".repeat(nameWidth)}-+-${"-".repeat(jdkWidth)}-+-${"-".repeat(bootWidth)}-+-${"-".repeat(cloudWidth)}-+")
}

data class CompatibilityDefinition(
    val name: String,
    val jdkVersion: JavaVersion,
    val springBootVersion: String,
    val springCloudVersion: String
) {
}
