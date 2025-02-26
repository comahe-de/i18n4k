package de.comahe.i18n4k.gradle.plugin

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.generator.GenerationTargetPlatform
import de.comahe.i18n4k.generator.I18n4kProcessor
import de.comahe.i18n4k.generator.Settings
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File


@Suppress("unused")
open class GenerateI18n4kFilesTask : DefaultTask() {

    @Internal
    lateinit var config: I18n4kExtension

    // Input parameter from [config] for "UP-TO-DATE" checks

    val packageName : String
        @Input
        get() = config.packageName ?: "<null>"

    val commentLanguage: String
        @Input
        get() = config.commentLocale ?: "<null>"

    val sourceCodeLocaleCodes: List<String>
        @Input
        get() = config.sourceCodeLocales ?: listOf("<null>")

    val messageFormatterClass  : String
        @Input
        get() = config.messageFormatter.javaClass.name

    val generationTarget  : String
        @Input
        get() = config.generationTargetPlatform?.name ?: "<null>"


    @InputDirectory
    open fun getInputDirectory(): File {
        var path = config.inputDirectory
        if (path == null)
            path = if (config.generationTargetPlatform == GenerationTargetPlatform.MULTI_PLATFORM)
                "src/commonMain/i18n" else "src/main/i18n"

        val inputDir = File(project.projectDir, path)
        if (!inputDir.isDirectory)
            throw GradleException("i18n4k input directory not found: ${inputDir.absolutePath}")
        return inputDir
    }

    @OutputDirectory
    open fun getGeneratedSourcesDirectory(): File {
        return I18n4kPlugin.getGeneratedSourcesDirectory(project, config)
    }

    @OutputDirectory
    open fun getGeneratedLanguageFilesDirectory(): File {
        return I18n4kPlugin.getGeneratedLanguageFilesDirectory(project, config)
    }


    @TaskAction
    fun doIt() {
        var generatedSourcesDirectory = getGeneratedLanguageFilesDirectory()
        val generatedLanguageFilesDirAndroidRawResourceStyle: Boolean
//        if (config.generationTargetPlatform == GenerationTargetPlatform.COMPOSE_MULTI_PLATFORM) {
//            generatedLanguageFilesDirAndroidRawResourceStyle = false
//            // files must be in the "files" subdirectory
//            generatedSourcesDirectory = File(generatedSourcesDirectory, "files")
//        } else
        if (config.generationTargetPlatform == GenerationTargetPlatform.ANDROID) {
            generatedLanguageFilesDirAndroidRawResourceStyle = true
            // files must be in the "raw" subdirectory
            generatedSourcesDirectory = File(generatedSourcesDirectory, "raw")
        } else {
            generatedLanguageFilesDirAndroidRawResourceStyle = false
        }

        val settings = Settings(inputDirectory = getInputDirectory(),
                                generatedSourcesDirectory = getGeneratedSourcesDirectory(),
                                generatedLanguageFilesDirectory = generatedSourcesDirectory,
                                generatedLanguageFilesDirAndroidRawResourceStyle = generatedLanguageFilesDirAndroidRawResourceStyle,
                                packageName = config.packageName,
                                commentLocale = config.commentLocale?.let { forLocaleTag(it) },
                                sourceCodeLocales = config.sourceCodeLocales?.map { forLocaleTag(it) },
                                messageFormatter = config.messageFormatter,
                                generationTarget = config.generationTargetPlatform!!,
                                logger = logger,
                                customFactories = config.customFactories,
                                globalLocaleAsDefault = config.globalLocaleAsDefault)
        I18n4kProcessor(settings).execute()
    }

}