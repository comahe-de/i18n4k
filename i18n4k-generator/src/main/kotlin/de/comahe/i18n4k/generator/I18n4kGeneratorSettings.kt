package de.comahe.i18n4k.generator

import de.comahe.i18n4k.messages.formatter.MessageFormatter
import java.io.File
import de.comahe.i18n4k.Locale

/**
 * Represents the configuration settings for a code generation process.
 * This includes paths for input and output directories, localization options, generation targets,
 * logging and additional customization options to control code generation behavior.
 */
data class I18n4kGeneratorSettings (
    /** The directory containing the input files for processing. */
    val inputDirectory: File,
    /** Directory where the generated Kotlin source code file should be stored */
    val generatedSourcesDirectory: File,
    /** Directory where the language file with the list of string be stored */
    val generatedLanguageFilesDirectory: File,
    /** don't use packages but file prefixes, lowercase file names, etc. */
    val generatedLanguageFilesDirAndroidRawResourceStyle: Boolean,
    /** The package name to be used for generated sources, if applicable. */
    val packageName: String?,
    /** The locale to be used for comments in the generated source code, if provided. */
    val commentLocale: Locale?,
    /**
     * for this Locales source code will be generated to have the translations in the Kotlin code without the need
     * to load external language file at runtime. null for all languages, empty for no language.
     */
    val sourceCodeLocales: List<Locale>?,
    val messageFormatter: MessageFormatter,
    /** The target platform for which the code is being generated (e.g., JVM, Android, etc.). */
    val generationTarget: GenerationTargetPlatform,
    /** Logger instance used for recording messages, warnings, and errors during the generation process. */
    val logger: org.slf4j.Logger,
    /** Indicates whether custom factories should be used in the generated code. */
    val customFactories: Boolean,
    /** Determines whether the global locale should be used as the default locale during localization. */
    val globalLocaleAsDefault: Boolean,
                                  )
