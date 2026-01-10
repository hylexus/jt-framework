import io.github.hylexus.jt.gradle.utils.JtFrameworkConfig.jtFrameworkConfig
import io.github.hylexus.jt.gradle.utils.logInfo2
import io.github.hylexus.jt.gradle.utils.logTip

plugins {
    id("java-library")
    id("io.spring.dependency-management")
    id("maven-publish")
    id("io.gitee.pkmer.pkmerboot-central-publisher") apply false
    id("signing")
    id("checkstyle")
}
printCompatibilityMatrix()
group = jtFrameworkConfig.projectGroupId
version = jtFrameworkConfig.projectVersion

run {
    jtFrameworkConfig.checkStyleTaskEnabled
    jtFrameworkConfig.debugModulefatJarEnabled
    jtFrameworkConfig.needSign
}

val mavenRepoConfig = jtFrameworkConfig.mavenRepoConfig

configure(allprojects) {
    group = jtFrameworkConfig.projectGroupId
    version = jtFrameworkConfig.projectVersion
}

// region Java
configure(subprojects) {

    version = jtFrameworkConfig.projectVersion

    if (!isJavaProject()) {
        return@configure
    }

    logInfo2("configuring project: ${project.name}")

    apply(plugin = "java-library")
    java {
        sourceCompatibility = obtainJavaVersion()
        targetCompatibility = obtainJavaVersion()
    }
    tasks.test {
        useJUnitPlatform()
        // https://github.com/gradle/gradle/issues/7773
        // systemProperties(System.getProperties().map { (k, v) -> k.toString() to v }.toMap())
    }
    tasks.withType<JavaCompile> {
        sourceCompatibility = obtainJavaVersion().majorVersion
        targetCompatibility = obtainJavaVersion().majorVersion
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:-options", "-Xlint:deprecation", "-parameters"))
        options.release.set(obtainJavaVersion().majorVersion.toInt())
    }
    tasks.compileTestJava {
        sourceCompatibility = obtainJavaVersion().majorVersion
        targetCompatibility = obtainJavaVersion().majorVersion
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

        docletOptions.source = obtainJavaVersion().majorVersion
        docletOptions.addBooleanOption("html5", true)

        isFailOnError = false
        version = jtFrameworkConfig.projectVersion
        logging.captureStandardError(LogLevel.INFO)
        logging.captureStandardOutput(LogLevel.INFO)
    }

    // region DependencyManagement
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
            mavenBom("org.springframework.boot:spring-boot-dependencies:${obtainSpringBootBomVersion()}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${obtainSpringCloudBomVersion()}")
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
            dependency("org.jspecify:jspecify:1.0.0")
            dependency("org.jetbrains:annotations:26.0.2")
        }

        group = jtFrameworkConfig.projectGroupId
        version = jtFrameworkConfig.projectVersion
    }
    // endregion DependencyManagement

    // region checkstyle
    // 严重影响构建时间
    if (jtFrameworkConfig.checkStyleTaskEnabled) {
        apply(plugin = "checkstyle")
        checkstyle {
            toolVersion = "10.23.0"
            configDirectory.set(rootProject.file("build-script/checkstyle/"))
        }
        tasks.withType<Checkstyle> {
            enabled = jtFrameworkConfig.checkStyleTaskEnabled
        }
        // tasks.withType<Checkstyle>().configureEach {
        //     javaLauncher = javaToolchains.launcherFor {
        //         languageVersion.set(JavaLanguageVersion.of(project.obtainJavaVersion().majorVersion))
        //     }
        // }
    } else {
        logTip("\tDisabling task [checkstyle] in project [${project.name}] (jt-framework.backend.build.checkstyle.enabled == false)")
    }
    // endregion checkstyle

    // region CommonDependencies
    dependencies {
        if (jtFrameworkConfig.checkStyleTaskEnabled) {
            // 最后一个支持 java8 的版本: 9.3
            checkstyle("com.puppycrawl.tools:checkstyle:9.3")
        }

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testCompileOnly("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")

        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        api("org.jspecify:jspecify")
        api("org.jetbrains:annotations")
    }
    // endregion CommonDependencies
}
// endregion Java


// region Maven
configure(subprojects) {
    if (!isJavaProject()) {
        return@configure
    }

    normalization {
        runtimeClasspath {
            ignore("META-INF/MANIFEST.MF")
        }
    }
    tasks.jar {
        manifest {
            manifest.attributes["Implementation-Title"] = project.name
            manifest.attributes["Implementation-Version"] = jtFrameworkConfig.projectVersion
            manifest.attributes["Automatic-Module-Name"] = project.name.replace('-', '.')
            // manifest.attributes["Created-By"] = "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"
            manifest.attributes["Created-By"] = "${System.getProperty("java.version")} (${System.getProperty("java.vendor")})"
            manifest.attributes["Minimum-Jdk-Version"] = obtainJavaVersion().majorVersion
            manifest.attributes["X-Requires-Java-Version"] = obtainJavaVersion().majorVersion
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

    if (isMavenPublication()) {
        val stagingRepositoryPath = jtFrameworkConfig.centralPortalArtifactsTempDir
        apply(plugin = "maven-publish")
        if (jtFrameworkConfig.centralPortalMavenRepoEnabled) {
            apply(plugin = "io.gitee.pkmer.pkmerboot-central-publisher")
            tasks.withType<io.gitee.pkmer.tasks.BundleTask>().configureEach {
                dependsOn(tasks.test, tasks.checkstyleTest, tasks.checkstyleMain)
                // 只有部分模块有这两个任务
                dependsOn(tasks.matching { it.name in setOf("compileJmhJava", "checkstyleJmh") })
            }
            // 延迟配置，在插件完全应用后再执行
            afterEvaluate {
                project.extensions.findByType<io.gitee.pkmer.extension.PkmerBootPluginExtension>()?.apply {
                    sonatypeMavenCentral {
                        stagingRepository.set(file(stagingRepositoryPath))
                        username.set(mavenRepoConfig.getProperty("maven-central-portal.username"))
                        password.set(mavenRepoConfig.getProperty("maven-central-portal.password"))
                        publishingType.set(io.gitee.pkmer.enums.PublishingType.USER_MANAGED)
                    }
                }
            }
        }

        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = jtFrameworkConfig.projectGroupId
                    artifactId = project.name
                    version = jtFrameworkConfig.projectVersion

                    from(components["java"])
                    artifact(sourcesJar)
                    artifact(javaDocJar)

                    pom {
                        name.set(project.name)
                        packaging = "jar"
                        description.set(project.name)
                        url.set(jtFrameworkConfig.projectScmUrl)

                        licenses {
                            license {
                                name.set(jtFrameworkConfig.projectLicenseName)
                                url.set(jtFrameworkConfig.projectLicenseUrl)
                                distribution.set(jtFrameworkConfig.projectLicenseDistribution)
                            }
                        }

                        scm {
                            url.set(jtFrameworkConfig.projectScmUrl)
                            connection.set(jtFrameworkConfig.projectScmConnection)
                            developerConnection.set(jtFrameworkConfig.projectScmDeveloperConnection)
                        }

                        developers {
                            developer {
                                id.set(jtFrameworkConfig.projectDeveloperId)
                                name.set(jtFrameworkConfig.projectDeveloperName)
                                email.set(jtFrameworkConfig.projectDeveloperEmail)
                            }
                        }

                        issueManagement {
                            system.set(jtFrameworkConfig.projectIssueManagementSystem)
                            url.set(jtFrameworkConfig.projectIssueManagementUrl)
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
                        if (jtFrameworkConfig.privateMavenRepoEnabled) {
                            maven {
                                name = "private"
                                url = uri(mavenRepoConfig.getProperty("privateRepo-release.url"))
                                credentials {
                                    username = mavenRepoConfig.getProperty("privateRepo-release.username")
                                    password = mavenRepoConfig.getProperty("privateRepo-release.password")
                                }
                            }
                        }
                        // 2. 发布到 GitHub Packages
                        if (jtFrameworkConfig.githubMavenRepoEnabled) {
                            maven {
                                name = "GitHubPackages"
                                url = uri(mavenRepoConfig.getProperty("github-pkg-jt-framework.url"))
                                credentials {
                                    username = System.getenv("GITHUB_ACTOR")
                                        ?: System.getProperty("gpr.user")
                                                ?: mavenRepoConfig.getProperty("github-pkg-jt-framework.username")

                                    password = System.getenv("GITHUB_TOKEN")
                                        ?: System.getProperty("gpr.key")
                                                ?: mavenRepoConfig.getProperty("github-pkg-jt-framework.password")
                                }
                            }
                        }
                        // 3. 发布到 Maven 中央仓库
                        // 已废弃: 新版中央仓库发版参考 io.gitee.pkmer.pkmerboot-central-publisher
                        // maven {
                        //     name = "centralPortal"
                        //     url = uri(mavenRepoConfig.getProperty("sonatype-staging.url"))
                        //     credentials {
                        //         username = mavenRepoConfig.getProperty("sonatype-staging.username")
                        //         password = mavenRepoConfig.getProperty("sonatype-staging.password")
                        //     }
                        // }

                        maven {
                            name = "centralPortalLocalArtifacts"
                            // Specify the local staging repo path in the configuration.
                            url = uri(stagingRepositoryPath)
                        }
                    }
                }
            }

        }

        if (jtFrameworkConfig.needSign) {
            apply(plugin = "signing")
            signing {
                // 如果需要签名
                // 记得将 build-script/gradle/debug-template.gradle.properties 中的 gpg 配置放到 ~/.gradle/gradle.properties
                sign(publishing.publications["maven"])
            }
        }
    }
}
// endregion Maven

fun Project.obtainJavaVersion(): JavaVersion = project.compatibilityDefinition().jdkVersion

fun Project.obtainSpringBootBomVersion(): String = project.compatibilityDefinition().springBootVersion

fun Project.obtainSpringCloudBomVersion(): String = project.compatibilityDefinition().springCloudVersion
