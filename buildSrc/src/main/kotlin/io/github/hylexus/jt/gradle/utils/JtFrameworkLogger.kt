package io.github.hylexus.jt.gradle.utils

import io.github.hylexus.jt.gradle.utils.JtFrameworkLogger.isLogEnabled
import org.gradle.api.Project

/**
 * 自定义日志工具
 *
 * - 日志等级：success/info/warning/error/debug
 * - debug 默认关闭，可通过 Gradle property 或系统 property 开启
 * - 支持图标 + ANSI 颜色
 */
object JtFrameworkLogger {
    private const val PREFIX = "jt-framework.backend.build.log."

    private val defaultEnabled = mapOf(
        "success" to true,
        "info" to true,
        "warning" to true,
        "error" to true,
        "debug" to false
    )

    fun Project.isLogEnabled(level: String): Boolean {
        val key = "$PREFIX$level"
        return try {
            val prop = (findProperty(key)?.toString()
                ?: System.getProperty(key))?.toBoolean()
            prop ?: defaultEnabled[level] ?: false
        } catch (_: Exception) {
            return defaultEnabled[level] ?: false
        }
    }
}

private object LoggerIcons {
    const val SUCCESS = "[✅ SUCCESS]"
    const val SUCCESS_2 = "[\uD83D\uDE80 SUCCESS]"
    const val SUCCESS_3 = "[🟢 SUCCESS]"
    const val INFO = "[ℹ️ INFO]"
    const val INFO_2 = "[\uD83D\uDCA1 INFO]"
    const val INFO_3 = "[\uD83D\uDCA1 TIP]"
    const val INFO_4 = "[\uD83D\uDD14 TIP]"
    const val WARNING = "[⚠️ WARNING]"
    const val WARNING_2 = "[\uD83D\uDD14 WARNING]"
    const val ERROR = "[❌ ERROR]"
    const val ERROR_2 = "[\uD83D\uDED1 ERROR]"
    const val DEBUG = "[🐞 DEBUG]"
}

private object LoggerColors {
    const val RESET = "\u001B[0m"
    const val GREEN = "\u001B[32m"
    const val BLUE = "\u001B[34m"
    const val YELLOW = "\u001B[33m"
    const val RED = "\u001B[31m"
    const val CYAN = "\u001B[36m"
}

// -------------------- 扩展方法 --------------------
@Suppress("unused")
fun Project.logSuccess(msg: String) {
    if (isLogEnabled("success")) logger.lifecycle("${LoggerColors.GREEN}${LoggerIcons.SUCCESS} $msg${LoggerColors.RESET}")
}

@Suppress("unused")
fun Project.logSuccess2(msg: String) {
    if (isLogEnabled("success")) logger.lifecycle("${LoggerColors.GREEN}${LoggerIcons.SUCCESS_2} $msg${LoggerColors.RESET}")
}

@Suppress("unused")
fun Project.logSuccess3(msg: String) {
    if (isLogEnabled("success")) logger.lifecycle("${LoggerColors.GREEN}${LoggerIcons.SUCCESS_3} $msg${LoggerColors.RESET}")
}

fun Project.logInfo(msg: String) {
    if (isLogEnabled("info")) logger.lifecycle("${LoggerColors.BLUE}${LoggerIcons.INFO} $msg${LoggerColors.RESET}")
}

fun Project.logInfo2(msg: String) {
    if (isLogEnabled("info")) logger.lifecycle("${LoggerColors.BLUE}${LoggerIcons.INFO_2} $msg${LoggerColors.RESET}")
}

fun Project.logTip(msg: String) {
    if (isLogEnabled("info")) logger.lifecycle("${LoggerColors.BLUE}${LoggerIcons.INFO_3} $msg${LoggerColors.RESET}")
}

fun Project.logTip2(msg: String) {
    if (isLogEnabled("info")) logger.lifecycle("${LoggerColors.GREEN}${LoggerIcons.INFO_4} $msg${LoggerColors.RESET}")
}

@Suppress("unused")
fun Project.logWarning(msg: String) {
    if (isLogEnabled("warning")) logger.lifecycle("${LoggerColors.YELLOW}${LoggerIcons.WARNING} $msg${LoggerColors.RESET}")
}

fun Project.logWarning2(msg: String) {
    if (isLogEnabled("warning")) logger.lifecycle("${LoggerColors.YELLOW}${LoggerIcons.WARNING_2} $msg${LoggerColors.RESET}")
}

@Suppress("unused")
fun Project.logError(msg: String) {
    if (isLogEnabled("error")) logger.lifecycle("${LoggerColors.RED}${LoggerIcons.ERROR} $msg${LoggerColors.RESET}")
}

@Suppress("unused")
fun Project.logError2(msg: String) {
    if (isLogEnabled("error")) logger.lifecycle("${LoggerColors.RED}${LoggerIcons.ERROR_2} $msg${LoggerColors.RESET}")
}

fun Project.logDebug(msg: String) {
    if (isLogEnabled("debug")) logger.lifecycle("${LoggerColors.CYAN}${LoggerIcons.DEBUG} $msg${LoggerColors.RESET}")
}
