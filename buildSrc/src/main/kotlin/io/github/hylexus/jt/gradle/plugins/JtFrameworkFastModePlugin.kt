package io.github.hylexus.jt.gradle.plugins

import io.github.hylexus.jt.gradle.utils.JtFrameworkConfig.jtFrameworkConfig
import io.github.hylexus.jt.gradle.utils.logTip
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 由于本项目中示例性的 Spring FatJar 模块很多(严重影响构建时间)
 *
 * 因此，在 fastMode == true 时，禁用一些任务以加快构建速度
 * @author hylexus
 */
class JtFrameworkFastModePlugin : Plugin<Project> {

    // 这几个任务严重影响构建时间
    private val disabledTasks = setOf(
        "bootJar",
        "bootDistZip",
        "bootDistTar",
        "distZip",
        "distTar",
    )

    override fun apply(project: Project) {
        val skipFatJar = !project.jtFrameworkConfig.debugModulefatJarEnabled
        val disabledTaskNames = kotlin.collections.HashSet<String>()
        if (skipFatJar) {
            disabledTasks.forEach { taskName ->
                project.tasks.findByName(taskName)?.let { task ->
                    task.enabled = false
                    disabledTaskNames.add(taskName)
                }
            }
            project.logTip("Disabling task: $disabledTaskNames in project [${project.name}] (jt-framework.backend.build.debug-module-fatjar.enabled == false)")
        }
    }

}
