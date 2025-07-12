package de.comahe.i18n4k.generator.tests

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.generator.GenerationTargetPlatform
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
        val processor = I18n4kProcessor(
            inputDirectory = inputDirectory,
            packageName = packageName,
            messageFormatter = i18n4k.messageFormatter,
            generatorSetting = generatorSetting.copy(
                generationTarget = target,
                generatedSourceDir = File(expectedGeneratedSourcesBaseDirectory, target.name),
                generatedLanguageFilesDir =  expectedGeneratedLanguageFilesDirectory,

                ),
            logger = LoggerFactory.getLogger("I18n4k-Processor")
        )
        processor.execute()
    }

}
