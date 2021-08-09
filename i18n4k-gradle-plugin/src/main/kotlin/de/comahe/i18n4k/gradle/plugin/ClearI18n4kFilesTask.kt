package de.comahe.i18n4k.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

@Suppress("unused")
open class ClearI18n4kFilesTask : DefaultTask() {

    @Internal
    lateinit var config: I18n4kExtension

    @TaskAction
    fun doIt() {
        I18n4kPlugin.getGeneratedSourcesDirectory(project, config).deleteRecursively()
        I18n4kPlugin.getGeneratedLanguageFilesDirectory(project, config).deleteRecursively()
    }

}