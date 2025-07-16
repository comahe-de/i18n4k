package de.comahe.i18n4k.generator

import de.comahe.i18n4k.Locale
import java.io.File

/**
 * General setting for code/file generation
 *
 * See [I18n4kGenerator]
 */
data class I18n4kGeneratorSettings(
    /** Directory where the generated Kotlin source code file should be stored */
    val generatedSourceDir: File,

    /** Directory where the language file with the list of string be stored */
    val generatedLanguageFilesDir: File,

    /** don't use packages but file prefixes, lowercase file names, etc. */
    val languageFilesDirAndroidRawResourceStyle: Boolean,

    /** Locale which message bundle content should be added as comment. Null for no comments. */
    val commentLocale: Locale?,

    /**
     * for this Locales source code will be generated to have the translations in the Kotlin code
     * without the need to load external language file at runtime. null for all languages, empty for
     * no language.
     */
    val sourceCodeLocales: List<Locale>?,

    /** The target platform for generation */
    val generationTarget: GenerationTargetPlatform,

    /**
     * Enable usage of generic LocalizedString factories, if the text files contain messages with
     * parameters with value type declaration.
     */
    val valueTypesEnabled: Boolean,

    /**
     * Mapping of value type names to fully qualified class names of real classes.
     *
     * If null (default), only the default value classes will be applied.
     *
     * Only evaluated if [valueTypesEnabled] is true
     */
    val valueTypesMapping: Map<String, String>,

    /** Add the `JsExport` annotation to the generated classes */
    val enableJsExportAnnotation: Boolean,
)
