package de.comahe.i18n4k.generator.tests

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.generator.GenerationTargetPlatform
import de.comahe.i18n4k.generator.I18n4kGeneratorSettings
import de.comahe.i18n4k.generator.I18n4kProcessor
import de.comahe.i18n4k.i18n4k
import org.slf4j.LoggerFactory
import java.io.File

/** Simple main function that generates the files out of "text_original" folder */
fun main() {
    val i18n4kConfig = I18n4kConfigDefault()
    i18n4kConfig.ignoreMessageParseErrors = false
    i18n4k = i18n4kConfig

    for (target in GenerationTargetPlatform.entries) {
        val settings = I18n4kGeneratorSettings(
            inputDirectory,
            generatedSourcesDirectory = File(expectedGeneratedSourcesDirectory, target.name),
            generatedLanguageFilesDirectory = expectedGeneratedLanguageFilesDirectory,
            generatedLanguageFilesDirAndroidRawResourceStyle = false,
            packageName = packageName,
            commentLocale = commentLocale,
            sourceCodeLocales = sourceCodeLocales,
            messageFormatter = i18n4k.messageFormatter,
            generationTarget = target,
            logger = LoggerFactory.getLogger("I18n4k-Processor"),
            customFactories = false,
            globalLocaleAsDefault = true
                                              )
        I18n4kProcessor(settings).execute()
    }

}
