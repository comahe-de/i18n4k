package de.comahe.i18n4k.generator

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import java.io.File

/** Reads the source files and generates the i18n4k files
 *
 * See [I18n4kGenerator] for most parameter descriptions */
class I18n4kProcessor(
    private val inputDirectory: File,
    private val packageName: String?,
    private val messageFormatter: MessageFormatter,
    /** General setting for code/file generation */
    private val generatorSetting: I18n4kGeneratorSettings,
    private val logger: org.slf4j.Logger

) {

    /** Starts the generation of the files */
    fun execute() {
        val bundles = MessagesDataBundles(messageFormatter)

        // Activate error messages on parse errors!
        i18n4k = I18n4kConfigDefault().apply { ignoreMessageParseErrors = false }

        // printing config...

        logger.info("I18n4k - starring generation...")
        logger.info("I18n4k - Base dir is: {}", File(".").absolutePath)
        logger.info("I18n4k - inputDirectory dir is: {}", inputDirectory.absolutePath)
        logger.info(
            "I18n4k - generatedSourcesDirectory dir is: {}",
            generatorSetting.generatedSourceDir.absolutePath
        )
        logger.info(
            "I18n4k - generatedLanguageFilesDirectory dir is: {}",
            generatorSetting.generatedLanguageFilesDir.absolutePath
        )
        logger.info("I18n4k - packageName is: {}", packageName)
        logger.info("I18n4k - commentLocale is: {}", generatorSetting.commentLocale)
        logger.info("I18n4k - sourceCodeLocales is: {}", generatorSetting.sourceCodeLocales)
        logger.info("I18n4k - generationTarget is: {}", generatorSetting.generationTarget)


        logger.info("I18n4k - Clearing generatedSourcesDirectory...")
        generatorSetting.generatedSourceDir.deleteRecursively()

        logger.info("I18n4k - Clearing generatedLanguageFilesDirectory...")
        generatorSetting.generatedLanguageFilesDir.deleteRecursively()

        // running...

        logger.info("I18n4k - Searching for language bundles")
        bundles.findLanguageBundles(inputDirectory, packageName)
        logger.info("I18n4k - Found language bundles: $bundles")
        bundles.bundles.values.forEach { data ->
            logger.info("I18n4k - Generating code for language bundle: ${data.name}")
            I18n4kGenerator(
                settings = generatorSetting,
                bundle = data,
            ).run()
        }
        logger.info("I18n4k - Finished!")
    }
}