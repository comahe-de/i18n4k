package de.comahe.i18n4k.gradle.plugin

import de.comahe.i18n4k.generator.GenerationTargetPlatform
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.toTag

@Suppress("RedundantNullableReturnType")
open class I18n4kExtension {

    /** Directory where the language files are searched.
     *
     * Default is null wich means "src/main/i18n" for normal projects and "src/commonMain/i18n" for multiplatform projects */
    var inputDirectory: String? = null

    /** Package name where the generated classes will be stored.
     *
     * If null (default) the path relative to the [inputDirectory]  will be used. */
    var packageName: String? = null

    /**
     * Locale tag ([toTag]) which message bundle content should be added as comment. Null for no comments.
     *
     * Default: "en"
     */
    var commentLocale: String? = "en"

    /** For which locale tags ([toTag]) an source code language mapping should be produced.
     *
     * source code language mappings don't need resource loading because the language string are included in the source code
     *
     * null (default) for all languages; empty for no language
     */
    var sourceCodeLocales: List<String>? = null

    /**
     * Folder where the generated source code is stored.
     *
     * Allowed parameters:
     * * {buildDir} - build directory
     *
     * Default value: {buildDir}/generated/sources/i18n4k
     * */
    var sourceCodeOutputDirectory: String = "{buildDir}/generated/sources/i18n4k"

    /**
     * Folder where languages without source code language mappings are stored. They must be loaded as resources at runtime.
     *
     * Allowed parameters:
     * * {buildDir} - build directory
     * * {projectDir} - project directory
     *
     * Default value: {buildDir}/generated/resources/i18n4k
     * */
    var languageFilesOutputDirectory: String = "{buildDir}/generated/resources/i18n4k"

    /** The used message formatter in the clients (for index counting of parameters) */
    var messageFormatter: MessageFormatter = MessageFormatterDefault

    /** Target platform for generation. Null for automatic detection  */
    var generationTargetPlatform: GenerationTargetPlatform? = null


    /**
     * Enable usage of generic LocalizedString factories, if the text files contain messages with
     * parameters with value type declaration.
     */
    var valueTypesEnabled = true

    /**
     * Mapping of value type names to fully qualified class/type names of real classes.
     *
     * If null (default), only the default value classes will be applied.
     *
     * Only evaluated if [valueTypesEnabled] is true
     */
    var valueTypeMapping: Map<String, String>? = null

    /** !EXPERIMENTAL! Add the `JsExport` annotation to the generated classes */
    var enableJsExportAnnotation: Boolean = false
}