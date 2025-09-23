package de.comahe.i18n4k.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction


@Suppress("unused")
open class ClearI18n4kFilesTask : DefaultTask() {

    @Internal
    lateinit var config: I18n4kExtension

    @get:InputDirectory
    val projectDirectory: Directory = project.layout.projectDirectory

    @get:InputDirectory
    val buildDirectory: Directory = project.layout.buildDirectory.get()


    @TaskAction
    fun doIt() {
        I18n4kPlugin.getGeneratedSourcesDirectory(buildDirectory, config).deleteRecursively()
        I18n4kPlugin.getGeneratedLanguageFilesDirectory(buildDirectory, projectDirectory, config).deleteRecursively()
    }

}