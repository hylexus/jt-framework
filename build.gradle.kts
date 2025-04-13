import java.util.*

plugins {
    id("java-library")
    id("io.spring.dependency-management")
    id("maven-publish")
    id("signing")
    id("checkstyle")
}
val defaultJavaVersion = getConfigAsString("defaultJavaVersion")
val maximumJavaVersion = getConfigAsString("maximumJavaVersion")

val defaultSpringBootBomVersion = getConfigAsString("defaultSpringBootBomVersion")
val maximumSpringBootBomVersion = JtFrameworkVersions.MAXIMUM_SPRING_BOOT_BOM_VERSION

val defaultSpringCloudBomVersion = getConfigAsString("defaultSpringCloudBomVersion")
val maximumSpringCloudBomVersion = getConfigAsString("maximumSpringCloudBomVersion")

val mavenRepoConfig = getMavenRepoConfig()
val modulesToPublishToMavenRepo = setOf(
    "jt-core",
    "jt-808-server-support",
    "jt-808-server-spring-boot-autoconfigure",
    "jt-808-server-spring-boot-starter-boot2",
    "jt-808-server-spring-boot-starter",
//        "jt-1078-server-support",
//        "jt-1078-server-spring-boot-autoconfigure",
//        "jt-1078-server-spring-boot-starter",
//        "jt-1078-server-spring-boot-starter-boot2",
//        "jt-dashboard-common",
//        "jt-dashboard-client",
//        "jt-dashboard-client-spring-boot-starter",
//        "jt-dashboard-server",
//        "jt-dashboard-server-spring-boot-starter",
)
val springBoot3Modules = setOf(
    "jt-808-server-sample-bare-boot3",
    "jt-808-server-spring-boot-starter",
    "jt-1078-server-spring-boot-starter",
    "jt-1078-server-sample-webflux-boot3",
    "jt-1078-server-sample-webmvc-boot3",
    "jt-dashboard-server",
    "jt-dashboard-server-spring-boot-starter",
    "jt-dashboard-client",
    "jt-dashboard-client-spring-boot-starter",
    "jt-demo-808-server-webflux-boot3",
    "jt-demo-808-server-webmvc-boot3",
    "jt-demo-1078-server-webflux-boot3",
    "jt-demo-1078-server-webmvc-boot3",
    "jt-demo-dashboard-webflux-boot3",
    "jt-demo-dashboard-webmvc-boot3",
)

configure(allprojects) {
    group = getConfigAsString("projectGroup")
    version = getProjectVersion()
}

// region Java
configure(subprojects) {
    if (!isJavaProject(project)) {
        return@configure
    }

    println("configure ....... " + project.name)

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        resolutionStrategy {
            cacheChangingModulesFor(0, TimeUnit.SECONDS)
        }
        applyMavenExclusions(false)
        generatedPomCustomization {
            enabled(false)
        }
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${obtainSpringBootBomVersion(project)}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${obtainSpringCloudBomVersion(project)}")
        }

        dependencies {
            // 其他依赖版本都由上面的 mavenBom 控制
            // 这里指定 mavenBom 中没有包含的依赖版本
            dependency("org.hibernate:hibernate-validator:8.0.0.Final")
            dependency("io.github.hylexus.oaks:oaks-common-utils:1.0.7") {
                exclude("org.projectlombok:lombok")
            }
            dependency("com.github.sarveswaran-m:util.concurrent.blockingMap:0.91")
            dependency("com.google.guava:guava:31.1-jre")
            dependency("com.google.code.findbugs:jsr305:3.0.2")
            dependency("com.google.code.findbugs:annotations:3.0.1")
            dependency("javax.annotation:javax.annotation-api:1.3.2")
            dependency("org.apache.commons:commons-collections4:4.4")
            dependency("org.bouncycastle:bcprov-jdk18on:1.78.1")
        }

        group = getConfigAsString("projectGroup")
        version = getProjectVersion()
    }

    apply(plugin = "java-library")
    java {
        sourceCompatibility = JavaVersion.toVersion(obtainJavaVersion(project))
        targetCompatibility = JavaVersion.toVersion(obtainJavaVersion(project))
    }

    repositories {
        extraMavenRepositoryUrls().forEach {
            maven(it)
        }
        mavenCentral()
        mavenLocal()
    }
    tasks.compileJava {
        sourceCompatibility = obtainJavaVersion(project)
        targetCompatibility = obtainJavaVersion(project)
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:-options", "-Xlint:deprecation", "-parameters"))
    }
    tasks.compileTestJava {
        sourceCompatibility = obtainJavaVersion(project)
        targetCompatibility = obtainJavaVersion(project)
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:-options", "-Xlint:deprecation", "-parameters"))
    }
    tasks.javadoc {
        val docletOptions = options as StandardJavadocDocletOptions
        description = "Generates project-level javadoc for use in -javadoc jar"

        docletOptions.encoding = "UTF-8"
        docletOptions.memberLevel = JavadocMemberLevel.PROTECTED
        docletOptions.author(true)
        docletOptions.header = project.name
        docletOptions.use(true)
        docletOptions.addStringOption("Xdoclint:none", "-quiet")
        docletOptions.charSet("UTF-8")
        docletOptions.addStringOption("charset", "UTF-8")

        docletOptions.version(true)
        docletOptions.links("https://docs.oracle.com/en/java/javase/11/docs/api")

        docletOptions.source = obtainJavaVersion(project)
        docletOptions.addBooleanOption("html5", true)

        isFailOnError = false
        version = getProjectVersion()
        logging.captureStandardError(LogLevel.INFO)
        logging.captureStandardOutput(LogLevel.INFO)
    }

    apply(plugin = "checkstyle")
    checkstyle {
        toolVersion = "10.9.1"
        configDirectory.set(rootProject.file("build-script/checkstyle/"))
    }
}
// endregion Java


// region Maven
configure(subprojects) {
    if (!isJavaProject(project)) {
        return@configure
    }
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    normalization {
        runtimeClasspath {
            ignore("META-INF/MANIFEST.MF")
        }
    }
    tasks.jar {
        manifest {
            manifest.attributes["Implementation-Title"] = project.name
            manifest.attributes["Implementation-Version"] = getProjectVersion()
            manifest.attributes["Automatic-Module-Name"] = project.name.replace('-', '.')
            manifest.attributes["Created-By"] = "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"
            // manifest.attributes["Created-By"] = "${System.getProperty("java.version")} (${System.getProperty("java.vendor")})"
            manifest.attributes["Minimum-Jdk-Version"] = obtainJavaVersion(project)
        }

        from(rootProject.projectDir) {
            include("LICENSE")
            include("NOTICE")
            into("META-INF")
            rename("LICENSE", "LICENSE.txt")
        }
    }

    val sourcesJar by tasks.registering(Jar::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier.set("sources")
        from(sourceSets.getByName("main").java.srcDirs)
    }

    val javaDocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks.named("javadoc"))
    }

    if (modulesToPublishToMavenRepo.contains(project.name)) {
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = getConfigAsString("projectGroup")
                    artifactId = project.name
                    version = getProjectVersion()

                    from(components["java"])
                    artifact(sourcesJar)
                    artifact(javaDocJar)

                    pom {
                        name.set(project.name)
                        packaging = "jar"
                        description.set(project.name)
                        url.set(getConfigAsString("projectScmUrl"))

                        licenses {
                            license {
                                name.set(getConfigAsString("projectLicenseName"))
                                url.set(getConfigAsString("projectLicenseUrl"))
                                distribution.set(getConfigAsString("projectLicenseDistribution"))
                            }
                        }

                        scm {
                            url.set(getConfigAsString("projectScmUrl"))
                            connection.set(getConfigAsString("projectScmConnection"))
                            developerConnection.set(getConfigAsString("projectScmDeveloperConnection"))
                        }

                        developers {
                            developer {
                                id.set(getConfigAsString("projectDeveloperId"))
                                name.set(getConfigAsString("projectDeveloperName"))
                                email.set(getConfigAsString("projectDeveloperEmail"))
                            }
                        }

                        issueManagement {
                            system.set(getConfigAsString("projectIssueManagementSystem"))
                            url.set(getConfigAsString("projectIssueManagementUrl"))
                        }

                        versionMapping {
                            usage("java-api") {
                                fromResolutionOf("runtimeClasspath")
                            }
                            usage("java-runtime") {
                                fromResolutionResult()
                            }
                        }
                    }

                    repositories {
                        // 1. 发布到你自己的私有仓库
                        // 1.1 将 build-script/maven/repo-credentials.debug-template.properties 另存到 ~/.gradle/repo-credentials.properties 然后修改用户名和密码等属性
                        // 1.2 在 ~/.gradle/gradle.properties 中配置 signing.keyId, signing.password, signing.secretKeyRingFile
                        maven {
                            name = "private"
                            url = uri(mavenRepoConfig.getProperty("privateRepo-release.url"))
                            credentials {
                                username = mavenRepoConfig.getProperty("privateRepo-release.username")
                                password = mavenRepoConfig.getProperty("privateRepo-release.password")
                            }
                        }

                        // 2. 发布到 Maven 中央仓库
                        maven {
                            name = "sonatype"
                            url = uri(mavenRepoConfig.getProperty("sonatype-staging.url"))
                            credentials {
                                username = mavenRepoConfig.getProperty("sonatype-staging.username")
                                password = mavenRepoConfig.getProperty("sonatype-staging.password")
                            }
                        }
                    }
                }
            }

        }

        signing {
            if (needSign()) {
                sign(publishing.publications["maven"])
            }
            ////// 在 ~/.gradle/gradle.properties 文件中配置:
            // 具体请参考模板文件: build-script/maven/debug-template.gradle.properties
            // signing.keyId = ABCDEFGH
            // signing.password = you-password
            // signing.secretKeyRingFile = /path/to/secret.gpg
        }
    }
}
// endregion Maven

fun isJavaProject(project: Project): Boolean {
    return (project.name != "docs") && (project.name != "samples")
}

fun obtainJavaVersion(project: Project) = if (springBoot3Modules.contains(project.name)) maximumJavaVersion else defaultJavaVersion

fun obtainSpringBootBomVersion(project: Project) = if (springBoot3Modules.contains(project.name)) maximumSpringBootBomVersion else defaultSpringBootBomVersion

fun obtainSpringCloudBomVersion(project: Project) = if (springBoot3Modules.contains(project.name)) maximumSpringCloudBomVersion else defaultSpringCloudBomVersion

fun getProjectVersion() = getConfigAsString("projectVersion")

fun getConfigAsString(key: String) = project.ext.get(key) as String

fun extraMavenRepositoryUrls(): List<String> {
    return listOf(
//        "https://mirrors.cloud.tencent.com/nexus/repository/maven-public",
//        "https://repo.huaweicloud.com/repository/maven",
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
        "https://repo.spring.io/release",
        "https://repo.spring.io/snapshot"
    )
}

fun needSign() = !rootProject.version.toString().lowercase().endsWith("snapshot")

@JvmName("getMavenRepoConfigJvm")
fun getMavenRepoConfig(): Properties {
    val properties = Properties()
    val fileName = "repo-credentials.properties"
    val repoCredentialFile = file(System.getProperty("user.home") + "/.gradle/${fileName}")
    if (file(repoCredentialFile).exists()) {
        logger.quiet("The maven repository credentials file <<${fileName}>> will be load from: ${repoCredentialFile.absolutePath}")
        properties.load(repoCredentialFile.inputStream())
    } else {
        logger.quiet("The maven repository credentials file <<${fileName} -> {}>> not found , use `debug-template.repo-credentials.properties` for debugging.", repoCredentialFile.absolutePath)
        properties.load(rootProject.file("build-script/maven/debug-template.repo-credentials.properties").inputStream())
    }
    return properties
}
