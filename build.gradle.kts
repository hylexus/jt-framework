import com.github.jk1.license.LicenseReportExtension
import com.github.jk1.license.render.InventoryHtmlReportRenderer
import com.github.jk1.license.render.JsonReportRenderer
import com.github.jk1.license.render.ReportRenderer
import com.github.jk1.license.render.TextReportRenderer
import groovy.json.JsonOutput
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
    id("com.github.jk1.dependency-license-report")
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

// Root report aggregates Java modules; each module also has its own report task.
configureDependencyLicenseReport(arrayOf(project) + subprojects.filter { it.isJavaProject() })

// region Java
configure(subprojects) {

    version = jtFrameworkConfig.projectVersion

    if (!isJavaProject()) {
        return@configure
    }

    logInfo2("configuring project: ${project.name}")

    apply(plugin = "java-library")
    apply(plugin = "com.github.jk1.dependency-license-report")
    configureDependencyLicenseReport(arrayOf(project))
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
            mavenBom("io.github.hylexus.xtream:xtream-codec-dependencies:0.5.0-rc.1")
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

        if (needLombok()) {
            compileOnly("org.projectlombok:lombok")
            annotationProcessor("org.projectlombok:lombok")
            testCompileOnly("org.projectlombok:lombok")
            testAnnotationProcessor("org.projectlombok:lombok")
        }

        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.mockito:mockito-core")
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

    // 为包含相关运行时依赖的模块提供匹配的 LGPL 库源码和 MPL 文件源码。
    val runtimeComponents = providers.provider {
        configurations.getByName("runtimeClasspath").incoming.resolutionResult.allComponents
    }
    val jump3rSources = providers.provider {
        val modules = runtimeComponents.get().mapNotNull {
            it.id as? org.gradle.api.artifacts.component.ModuleComponentIdentifier
        }.filter { it.group == "de.sciss" && it.module == "jump3r" }
        modules.flatMap { module ->
            val sourceDependency = dependencies.create("${module.group}:${module.module}:${module.version}:sources@jar")
            configurations.detachedConfiguration(sourceDependency).apply {
                isTransitive = false
            }.files
        }
    }
    val symphoniaSource = providers.provider {
        val usesJt1078 = path == ":jt-1078-server-support" || runtimeComponents.get().any {
            (it.id as? org.gradle.api.artifacts.component.ProjectComponentIdentifier)?.projectPath == ":jt-1078-server-support"
        }
        if (usesJt1078) listOf(rootProject.file(
            "jt-1078-server-support/src/main/java/io/github/hylexus/jt/jt1078/support/extension/audio/impl/converters/AdpcmImaToPcmJt1078AudioFormatConverter.java"
        )) else emptyList()
    }
    val prepareThirdPartySources = tasks.register<Sync>("prepareThirdPartySources") {
        group = "reporting"
        description = "Collect matching jump3r sources and the Symphonia-derived source when used by this module."
        into(layout.buildDirectory.dir("generated/third-party-sources"))
        from(jump3rSources) { into("jump3r") }
        from(providers.provider {
            if (jump3rSources.get().isNotEmpty()) listOf(rootProject.file("licenses/jump3r")) else emptyList()
        }) {
            into("jump3r")
            exclude("**/__pycache__/**", "**/*.pyc")
        }
        from(symphoniaSource) { into("symphonia") }
    }

    // 每种 JAR 都包含项目许可和当前模块的第三方声明。
    tasks.withType<Jar>().configureEach {
        from(rootProject.projectDir) {
            include("LICENSE", "NOTICE", "THIRD-PARTY-SOURCE-NOTICES.txt", "THIRD-PARTY-FRONTEND-NOTICES.txt")
            into("META-INF")
            rename("LICENSE", "LICENSE.txt")
        }
        from(rootProject.file("licenses")) {
            into("META-INF/licenses")
            exclude("**/__pycache__/**", "**/*.pyc")
            // Repository audit/build inputs; retain original notices and source records in the JAR.
            exclude("source-audit.json", "frontend-inventory.json", "docs-frontend-inventory.json")
        }
        // Supply the editable LGPL-derived build configuration with its notices.
        from(rootProject.file("build-script/checkstyle/checkstyle.xml")) {
            into("META-INF/licenses/checkstyle")
            rename("checkstyle.xml", "jt-framework-checkstyle.xml")
        }
        from(tasks.named("generateLicenseReport")) {
            include("THIRD-PARTY-NOTICES.txt")
            into("META-INF")
        }
        from(prepareThirdPartySources) {
            into("META-INF/third-party-sources")
        }
    }

    // 完整应用的分发材料按需生成到 build/，不进入普通 JAR 的构建链。
    pluginManager.withPlugin("org.springframework.boot") {
        val bootJar = tasks.named<Jar>("bootJar")
        val distributionArtifacts = providers.provider {
            configurations.getByName("runtimeClasspath").resolvedConfiguration.resolvedArtifacts
                .filter { it.id.componentIdentifier is org.gradle.api.artifacts.component.ModuleComponentIdentifier }
                .sortedBy { "${it.moduleVersion.id}:${it.file.name}" }
        }
        val sourceModules = setOf(
            "ch.qos.logback:logback-classic", "ch.qos.logback:logback-core",
            "jakarta.annotation:jakarta.annotation-api", "net.jcip:jcip-annotations",
            "com.google.code.findbugs:jsr305",
            "org.bouncycastle:bcprov-jdk18on", "org.slf4j:slf4j-api", "org.slf4j:jul-to-slf4j",
        )
        val distributionSources = providers.provider {
            distributionArtifacts.get().filter {
                "${it.moduleVersion.id.group}:${it.moduleVersion.id.name}" in sourceModules
            }.associate { artifact ->
                val coordinate = artifact.moduleVersion.id.toString()
                val dependency = dependencies.create("$coordinate:sources@jar")
                coordinate to configurations.detachedConfiguration(dependency).apply { isTransitive = false }.singleFile
            }
        }
        tasks.register<Exec>("prepareDistributionLicenses") {
            group = "reporting"
            description = "Prepare a separate license/source bundle for the actual Boot JAR; no publishing."
            dependsOn(bootJar, tasks.named("generateLicenseReport"))
            inputs.file(rootProject.file("build-script/prepare-distribution-licenses.py"))
            inputs.property("bootJarEnabled", bootJar.map { it.enabled })
            inputs.file(bootJar.flatMap { it.archiveFile })
            inputs.files(distributionSources.map { it.values })
            inputs.dir(layout.buildDirectory.dir("reports/dependency-license"))
            val output = layout.buildDirectory.dir("reports/distribution-licenses")
            outputs.dir(output)
            outputs.file(output.map { it.asFile.resolveSibling("distribution-licenses.zip") })
            doFirst {
                if (!bootJar.get().enabled) {
                    throw GradleException("Enable bootJar with -Pjt-framework.backend.build.debug-module-fatjar.enabled=true before preparing distribution materials.")
                }
                val input = layout.buildDirectory.file("generated/distribution-license-input.json").get().asFile
                input.parentFile.mkdirs()
                input.writeText(JsonOutput.toJson(mapOf(
                    "archive" to bootJar.get().archiveFile.get().asFile.absolutePath,
                    "report" to layout.buildDirectory.file("reports/dependency-license/THIRD-PARTY-NOTICES.txt").get().asFile.absolutePath,
                    "artifacts" to distributionArtifacts.get().map { artifact -> mapOf(
                        "coordinate" to artifact.moduleVersion.id.toString(),
                        "filename" to artifact.file.name,
                        "source" to distributionSources.get()[artifact.moduleVersion.id.toString()]?.absolutePath,
                    ) },
                )))
                val arguments = mutableListOf(
                    "python3", rootProject.file("build-script/prepare-distribution-licenses.py").absolutePath,
                    "--input", input.absolutePath, "--output", output.get().asFile.absolutePath,
                    "--cache", rootProject.layout.buildDirectory.dir("license-downloads").get().asFile.absolutePath,
                )
                if (gradle.startParameter.isOffline) arguments.add("--offline")
                commandLine(arguments)
            }
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
        val stagingRepositoryPath = jtFrameworkConfig.centralPortalArtifactsTempDir + "/${project.name}"
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

// Check actual artifacts without publishing or retaining extra dependency copies.
val licenseCheckJars = subprojects.filter { it.isJavaProject() }.flatMap { module ->
    val names = if (module.isMavenPublication()) listOf("jar", "sourcesJar", "javaDocJar") else listOf("jar")
    names.map { module.tasks.named<Jar>(it) }
}
tasks.register<Exec>("verifyJarLicenseMaterials") {
    group = "verification"
    description = "Check license materials in Java module JARs and Maven publication source/Javadoc JARs."
    dependsOn(licenseCheckJars)
    doFirst {
        commandLine(listOf(
            "python3", rootProject.file("build-script/check-license-materials.py").absolutePath,
            "--report", layout.buildDirectory.file("reports/license-check/jars.txt").get().asFile.absolutePath,
            "jars",
        ) + licenseCheckJars.map { it.get().archiveFile.get().asFile.absolutePath })
    }
}

fun Project.configureDependencyLicenseReport(reportProjects: Array<Project>) {
    extensions.configure<LicenseReportExtension> {
        projects = reportProjects
        configurations = arrayOf("compileClasspath", "runtimeClasspath", "annotationProcessor")
        outputDir = layout.buildDirectory.dir("reports/dependency-license").get().asFile.absolutePath
        excludeOwnGroup = true
        excludeBoms = true
        // Prefer the artifact's own declaration; inherit parent licenses only when it has none.
        unionParentPomLicenses = false
        renderers = arrayOf<ReportRenderer>(
            InventoryHtmlReportRenderer("index.html", "${project.name} dependency licenses"),
            // Keep every discovered license; dependencies may have multiple license terms.
            JsonReportRenderer("index.json", false),
            TextReportRenderer("THIRD-PARTY-NOTICES.txt"),
        )
    }
}

fun Project.obtainJavaVersion(): JavaVersion = project.compatibilityDefinition().jdkVersion

fun Project.obtainSpringBootBomVersion(): String = project.compatibilityDefinition().springBootVersion

fun Project.obtainSpringCloudBomVersion(): String = project.compatibilityDefinition().springCloudVersion
