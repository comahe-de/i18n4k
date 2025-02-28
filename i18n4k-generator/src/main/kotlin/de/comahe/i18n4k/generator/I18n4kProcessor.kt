package de.comahe.i18n4k.generator

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import java.io.File

/** Reads the source files and generates the i18n4k files
 *
 * See [I18n4kGenerator] for most parameter descriptions */
class I18n4kProcessor(val settings: I18n4kGeneratorSettings) {

    /** Starts the generation of the files */
    fun execute() {
        val bundles = MessagesDataBundles(settings.messageFormatter)

        // Activate error messages on parse errors!
        i18n4k = I18n4kConfigDefault().apply { ignoreMessageParseErrors = false }

        // printing config...
        val logger = settings.logger
        logger.info("I18n4k - starring generation...")
        logger.info("I18n4k - Base dir is: {}", File(".").absolutePath)
        logger.info("I18n4k - inputDirectory dir is: {}", settings.inputDirectory.absolutePath)
        logger.info(
            "I18n4k - generatedSourcesDirectory dir is: {}",
            settings.generatedSourcesDirectory.absolutePath
        )
        logger.info(
            "I18n4k - generatedLanguageFilesDirectory dir is: {}",
            settings.generatedLanguageFilesDirectory.absolutePath
        )
        logger.info("I18n4k - packageName is: {}", settings.packageName)
        logger.info("I18n4k - commentLocale is: {}", settings.commentLocale)
        logger.info("I18n4k - sourceCodeLocales is: {}", settings.sourceCodeLocales)
        logger.info("I18n4k - generationTarget is: {}", settings.generationTarget)


        logger.info("I18n4k - Clearing generatedSourcesDirectory...")
        settings.generatedSourcesDirectory.deleteRecursively()

        logger.info("I18n4k - Clearing generatedLanguageFilesDirectory...")
        settings.generatedLanguageFilesDirectory.deleteRecursively()

        // running...

        logger.info("I18n4k - Searching for language bundles")
        bundles.findLanguageBundles(settings.inputDirectory, settings.packageName)
        logger.info("I18n4k - Found language bundles: $bundles")
        bundles.bundles.values.forEach { data ->
            logger.info("I18n4k - Generating code for language bundle: ${data.name}")
            I18n4kGenerator(settings, data).run()
        }
        logger.info("I18n4k - Finished!")
    }
}