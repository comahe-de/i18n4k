package de.comahe.i18n4k.generator

import de.comahe.i18n4k.messages.formatter.MessageFormatter
import java.io.File
import de.comahe.i18n4k.Locale

data class Settings(
    val inputDirectory: File,
    val generatedSourcesDirectory: File,
    val generatedLanguageFilesDirectory: File,
    val generatedLanguageFilesDirAndroidRawResourceStyle: Boolean,
    val packageName: String?,
    val commentLocale: Locale?,
    val sourceCodeLocales: List<Locale>?,
    val messageFormatter: MessageFormatter,
    val generationTarget: GenerationTargetPlatform,
    val logger: org.slf4j.Logger,
    val customFactories: Boolean,
                   )
