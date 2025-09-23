package de.comahe.i18n4k.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File


@Suppress("unused")
open class ClearI18n4kFilesTask : DefaultTask() {

    @Internal
    lateinit var config: I18n4kExtension

    @get:Input
    val projectDirectory: String = project.layout.projectDirectory.asFile.absolutePath

    @get:Input
    val buildDirectory: String = project.layout.buildDirectory.get().asFile.absolutePath


    @TaskAction
    fun doIt() {
        I18n4kPlugin.getGeneratedSourcesDirectory(File(buildDirectory), config).deleteRecursively()
        I18n4kPlugin.getGeneratedLanguageFilesDirectory(File(buildDirectory), File(projectDirectory), config).deleteRecursively()
    }

}