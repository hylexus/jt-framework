package io.github.hylexus.jt.gradle.utils

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.io.File
import java.util.*


object JtFrameworkConfig {
    private const val CACHE_KEY = "jtFrameworkConfigCache"

    /**
     * 给 Project 提供唯一入口，避免全局扩展属性冲突。
     * 使用 rootProject.extra 保证缓存与 Gradle 生命周期绑定，
     * 支持多模块共享、避免跨工程污染。
     */
    val Project.jtFrameworkConfig: Config
        get() {
            return rootProject.extensions.findByType(Config::class.java)
                ?: Config(this).also {
                    rootProject.extensions.add("jtFrameworkConfig", it)
                }
        }

    private val thirdpartyDependencies: List<ThirdPartyDependency> = listOf(
        ThirdPartyDependency("com.googlecode.aviator:aviator", "5.4.3", "com.googlecode.aviator.version"),
        ThirdPartyDependency("org.mvel:mvel2", "2.5.2.Final", "org.mvel.mvel2.version"),
    )

    /**
     * 配置容器类
     */
    class Config(private val project: Project) {

        private val cachedProperties: MutableMap<String, Any>
            get() = project.rootProject.extra.let { extra ->
                if (!extra.has(CACHE_KEY)) {
                    extra.set(CACHE_KEY, mutableMapOf<String, Any>())
                }
                @Suppress("UNCHECKED_CAST")
                extra.get(CACHE_KEY) as MutableMap<String, Any>
            }

        /** 通用缓存读取方法 */
        fun <T> getOrLoadConfig(key: String, loader: Project.() -> T): T {
            @Suppress("UNCHECKED_CAST")
            return cachedProperties[key] as? T ?: project.loader().also { cachedProperties[key] = it as Any }
        }

        /** 读取字符串配置，支持缓存 */
        fun getOrLoadConfigAsString(key: String, default: String? = null): String {
            return getOrLoadConfig(key) { loadConfigAsString(key, default) }
        }

        /** 读取布尔配置，支持缓存 */
        fun getOrLoadConfigAsBoolean(key: String, default: Boolean? = null): Boolean {
            return getOrLoadConfig(key) {
                loadConfigAsString(key, (default ?: false).toString())
            }.toBoolean()
        }

        fun getOrLoadConfigAsBoolean(key: String, default: Boolean): Boolean {
            val strValue = getOrLoadConfig(key) { loadConfigAsString(key, null) }
            @Suppress("UNNECESSARY_SAFE_CALL")
            return when (strValue?.lowercase()) {
                "true", "on", "yes" -> true
                "false", "off", "no" -> false
                null -> default
                else -> error("Invalid boolean value for $key: '$strValue'")
            }
        }

        /**
         * 读取配置的最终逻辑：
         * 1. Gradle properties (-Pkey=val 或 gradle.properties)
         * 2. System properties (-Dkey=val)
         * 3. 环境变量（自动将 key 转换为 ENV_KEY）
         */
        fun loadConfigAsString(key: String, default: String? = null): String {
            // 1. Gradle properties
            project.findProperty(key)?.toString()?.let { value ->
                project.logDebug("Loading config [Gradle property]: $key = ${filterValue(key, value)}")
                return value
            }

            // 2. System properties
            System.getProperty(key)?.let { value ->
                project.logDebug("Loading config [System property]: $key = ${filterValue(key, value)}")
                return value
            }

            // 3. 环境变量（自动转换 key → ENV_KEY）
            val envKey = key.replace('.', '_')
                .replace('-', '_')
                .uppercase()

            System.getenv(envKey)?.let { value ->
                project.logDebug("Loading config [Environment variable]: $envKey = ${filterValue(envKey, value)} (mapped from $key)")
                return value
            }

            return default
                ?: error("Config property '$key' not found in Gradle properties, System properties, or Environment variables")
        }

        // project-info
        val projectGroupId: String get() = getOrLoadConfigAsString("projectGroup")

        /** 获取项目版本，支持 CI 自动版本生成 */
        val projectVersion: String
            get() = getOrLoadConfig("projectVersion") {
                val baseVersion = getOrLoadConfigAsString("projectVersion")

                // 判断是否是预发布版本（alpha, beta, rc）
                val isPrerelease = listOf("alpha", "beta", "rc").any { baseVersion.contains(it, ignoreCase = true) }
                // 如果不是预发布，直接返回原版本
                if (!isPrerelease) return@getOrLoadConfig baseVersion

                // 检查是否在 GitHub Actions 环境
                val isRunningInCI = System.getenv("GITHUB_ACTIONS") == "true"
                val ciRunNumber = System.getenv("GITHUB_RUN_NUMBER")
                val gitSha = System.getenv("GITHUB_SHA")?.take(7)

                if (isRunningInCI && !ciRunNumber.isNullOrBlank() && !gitSha.isNullOrBlank()) {
                    // CI 环境：生成带 run_number 和 git hash 的版本
                    // 例如：baseVersion = "0.1.1-alpha.0"
                    // dropLastWhile { it != '.' } ==> "0.1.1-alpha."
                    // dropLast(1) ==> "0.1.1-alpha"
                    // 最终 → "0.1.1-alpha.123.git.abc1234"
                    "${baseVersion.dropLastWhile { it != '.' }.dropLast(1)}.${ciRunNumber}.git.${gitSha}"
                } else {
                    baseVersion
                }
            }

        val projectDeveloperId: String get() = getOrLoadConfigAsString("projectDeveloperId")
        val projectDeveloperName: String get() = getOrLoadConfigAsString("projectDeveloperName")
        val projectDeveloperEmail: String get() = getOrLoadConfigAsString("projectDeveloperEmail")

        // issue
        val projectHomePage: String get() = getOrLoadConfigAsString("projectHomePage")
        val projectIssueManagementSystem: String get() = getOrLoadConfigAsString("projectIssueManagementSystem")
        val projectIssueManagementUrl: String get() = getOrLoadConfigAsString("projectIssueManagementUrl")

        // scm
        val projectScmUrl: String get() = getOrLoadConfigAsString("projectScmUrl")
        val projectScmConnection: String get() = getOrLoadConfigAsString("projectScmConnection")
        val projectScmDeveloperConnection: String get() = getOrLoadConfigAsString("projectScmDeveloperConnection")

        // license(Apache License 2.0)
        val projectLicenseName: String get() = getOrLoadConfigAsString("projectLicenseName")
        val projectLicenseUrl: String get() = getOrLoadConfigAsString("projectLicenseUrl")
        val projectLicenseDistribution: String get() = getOrLoadConfigAsString("projectLicenseDistribution")

        // build
        val debugModulefatJarEnabled: Boolean get() = getOrLoadConfigAsBoolean("jt-framework.backend.build.debug-module-fatjar.enabled", false)
        val checkStyleTaskEnabled: Boolean
            get() {
                return getOrLoadConfigAsBoolean("jt-framework.backend.build.checkstyle.enabled", false)
            }

        // Maven
        val mavenRepoConfig: Properties
            get() = getOrLoadConfig("mavenRepoConfig") {
                val properties = Properties()
                val fileName = "repo-credentials.properties"
                val repoCredentialFile = File(System.getProperty("user.home"), ".gradle/$fileName")

                if (repoCredentialFile.exists()) {
                    project.logInfo("Loading Maven repo credentials from: ${repoCredentialFile.absolutePath}")
                    properties.load(repoCredentialFile.inputStream())
                } else {
                    project.logTip2("Maven repo credentials not found, using debug-template")
                    properties.load(project.rootProject.file("build-script/maven/debug-template.repo-credentials.properties").inputStream())
                }
                properties
            }
        val privateMavenRepoEnabled: Boolean get() = getOrLoadConfigAsBoolean("jt-framework.maven.repo.private.enabled")
        val githubMavenRepoEnabled: Boolean get() = getOrLoadConfigAsBoolean("jt-framework.maven.repo.github.enabled")
        val centralPortalMavenRepoEnabled: Boolean get() = getOrLoadConfigAsBoolean("jt-framework.maven.repo.central-portal.enabled")
        val centralPortalArtifactsTempDir: String
            get() {
                val key = "jt-framework.maven.repo.central-portal.artifacts.temp-dir"
                return getOrLoadConfig(key) {
                    val result = loadConfigAsString(key, null)
                    file(result).mkdirs()
                    result
                }
            }
        val needSign: Boolean
            get() {
                val key = "jt-framework.maven.publications.signing"
                return getOrLoadConfig(key) {
                    when (val signingConfigValue = loadConfigAsString(key, "auto")) {
                        "on" -> true
                        "off" -> {
                            logWarning2("MavenPublications Signing is disabled.")
                            false
                        }

                        "auto" -> {
                            val version = projectVersion.lowercase()
                            val result: Boolean = !version.endsWith("-snapshot")
                                    && !version.contains("-alpha")
                                    && !version.contains("-beta")
                                    && !version.contains("-rc")
                            if (!result) {
                                logWarning2("MavenPublications Signing is disabled. $key = [$signingConfigValue --> false]")
                            }
                            result
                        }

                        else -> throw IllegalArgumentException("Invalid signing config value: $signingConfigValue")
                    }
                }
            }

        /** 敏感信息过滤 */
        private fun filterValue(key: String, value: String): String =
            if (key.contains("password", ignoreCase = true) ||
                key.contains("secret", ignoreCase = true) ||
                key.contains("token", ignoreCase = true)
            ) "******" else value
    }

}

data class ThirdPartyDependency(val name: String, val version: String, val versionPropertyName: String) {
    fun toIdentifier(): String = "$name:$version"
}
