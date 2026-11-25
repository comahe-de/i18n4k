package de.comahe.i18n4k.toolchain.tasks

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.generator.GenerationTargetPlatform
import de.comahe.i18n4k.generator.I18n4kGeneratorSettings
import de.comahe.i18n4k.generator.I18n4kProcessor
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.toolchain.GenerationTarget
import org.jetbrains.amper.plugins.Input
import org.jetbrains.amper.plugins.Output
import org.jetbrains.amper.plugins.TaskAction
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.isDirectory

/**
 * Generates the i18n4k Kotlin sources and language resource files.
 *
 * All the work is done by [I18n4kProcessor] from the `i18n4k-generator`
 * library; this task only adapts the Kotlin Toolchain inputs to it.
 *
 * The outputs are declared in `plugin.yaml` under `generated.sources` and
 * `generated.resources`, so the Kotlin Toolchain (and the IDE) treat them as
 * regular source and resource directories of the module.
 */
@TaskAction
fun generateI18n4kFiles(
    @Input inputDirectory: Path,
    @Output generatedSourcesDir: Path,
    @Output generatedLanguageFilesDir: Path,
    packageName: String?,
    commentLocale: String?,
    sourceCodeLocales: List<String>?,
    generationTarget: GenerationTarget,
    valueTypesEnabled: Boolean,
    valueTypeMapping: Map<String, String>,
    enableJsExportAnnotation: Boolean,
) {
    require(inputDirectory.isDirectory()) {
        "i18n4k input directory not found: ${inputDirectory.absolutePathString()}. " +
            "Create it or set 'plugins.i18n4k.inputDirectory' in module.yaml."
    }

    val targetPlatform = generationTarget.toGeneratorTarget()

    // Android needs the "raw resource" file layout (flat directory, snake_case
    // names). Clear the parent as well so switching the target does not leave
    // stale files behind.
    val languageFilesDir = if (targetPlatform == GenerationTargetPlatform.ANDROID) {
        generatedLanguageFilesDir.toFile().deleteRecursively()
        generatedLanguageFilesDir.resolve("raw")
    } else {
        generatedLanguageFilesDir
    }

    I18n4kProcessor(
        inputDirectory = inputDirectory.toFile(),
        packageName = packageName,
        messageFormatter = MessageFormatterDefault,
        generatorSetting = I18n4kGeneratorSettings(
            generatedSourceDir = generatedSourcesDir.toFile(),
            generatedLanguageFilesDir = languageFilesDir.toFile(),
            languageFilesDirAndroidRawResourceStyle = targetPlatform == GenerationTargetPlatform.ANDROID,
            commentLocale = commentLocale?.let { forLocaleTag(it) },
            sourceCodeLocales = sourceCodeLocales?.map { forLocaleTag(it) },
            generationTarget = targetPlatform,
            valueTypesEnabled = valueTypesEnabled,
            valueTypesMapping = valueTypeMapping,
            enableJsExportAnnotation = enableJsExportAnnotation,
        ),
        logger = PrintlnLogger,
    ).execute()
}

private fun GenerationTarget.toGeneratorTarget(): GenerationTargetPlatform =
    when (this) {
        GenerationTarget.JVM -> GenerationTargetPlatform.JVM
        GenerationTarget.ANDROID -> GenerationTargetPlatform.ANDROID
        GenerationTarget.JS -> GenerationTargetPlatform.JS
        GenerationTarget.NATIVE -> GenerationTargetPlatform.NATIVE
        GenerationTarget.MULTI_PLATFORM -> GenerationTargetPlatform.MULTI_PLATFORM
    }
