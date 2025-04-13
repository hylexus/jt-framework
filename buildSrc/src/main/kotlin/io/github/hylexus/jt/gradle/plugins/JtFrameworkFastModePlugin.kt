package io.github.hylexus.jt.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project


class JtFrameworkFastModePlugin : Plugin<Project> {
    private val disabledTasks = setOf(
        "bootJar",
        "bootDistZip",
        "bootDistTar",
        "distZip",
        "distTar",
    )

    override fun apply(project: Project) {
        val fastMode = project.findProperty("fastMode")?.toString()?.toBoolean() ?: false
        val fastModeLogging = project.findProperty("fastModeLogging")?.toString()?.toBoolean() ?: false
        val disabledTaskNames = HashSet<String>()
        if (fastMode) {
            disabledTasks.forEach { taskName ->
                project.tasks.findByName(taskName)?.let { task ->
                    task.enabled = false
                    disabledTaskNames.add(taskName)
                }
            }
            if (fastModeLogging) {
                println("Disabling task: $disabledTaskNames in project ${project.name}(fastMode == true)")
            }
        }
    }
}
