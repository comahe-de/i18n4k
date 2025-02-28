package de.comahe.i18n4k.generator.tests

import de.comahe.i18n4k.generator.GenerationTargetPlatform
import de.comahe.i18n4k.generator.I18n4kGeneratorSettings
import de.comahe.i18n4k.generator.I18n4kProcessor
import de.comahe.i18n4k.i18n4k
import org.slf4j.LoggerFactory
import java.io.File

/** Simple main function that generates the files out of "text_original" folder */
fun main() {

    for (target in GenerationTargetPlatform.values()) {
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
