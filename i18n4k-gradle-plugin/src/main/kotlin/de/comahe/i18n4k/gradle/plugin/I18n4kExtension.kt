package de.comahe.i18n4k.gradle.plugin

import de.comahe.i18n4k.generator.GenerationTargetPlatform
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.toTag
import org.gradle.api.tasks.*

@Suppress("RedundantNullableReturnType")
open class I18n4kExtension {

    /** Directory where the language files are searched.
     *
     * Default is null wich means "src/main/i18n" for normal projects and "src/commonMain/i18n" for multiplatform projects */
    @Input
    @Optional
    var inputDirectory: String? = null

    /** Package name where the generated classes will be stored.
     *
     * If null (default) the path relative to the [inputDirectory]  will be used. */
    @Input
    @Optional
    var packageName: String? = null

    /**
     * Locale tag ([toTag]) which message bundle content should be added as comment. Null for no comments.
     *
     * Default: "en"
     */
    @Input
    @Optional
    val commentLocale: String? = "en"

    /** For which locale tags ([toTag]) an source code language mapping should be produced.
     *
     * source code language mappings don't need resource loading because the language string are included in the source code
     *
     * null (default) for all languages; empty for no language
     */
    @Input
    @Optional
    var sourceCodeLocales: List<String>? = null

    /**
     * Folder where the generated source code is stored.
     *
     * Allowed parameters:
     * * {buildDir} - build directory
     *
     * Default value: {buildDir}/generated/sources/i18n4k
     * */
    @Input
    var sourceCodeOutputDirectory: String = "{buildDir}/generated/sources/i18n4k"

    /**
     * Folder where languages without source code language mappings are stored. They must be loaded as resources at runtime.
     *
     * Allowed parameters:
     * * {buildDir} - build directory
     *
     * Default value: {buildDir}/generated/resources/i18n4k
     * */
    @Input
    var languageFilesOutputDirectory: String = "{buildDir}/generated/resources/i18n4k"

    /** The used message formatter in the clients (for index counting of parameters) */
    @Internal
    var messageFormatter: MessageFormatter = MessageFormatterDefault

    /** Target platform for generation. Null for automatic detection  */
    @Input
    @Optional
    var generationTargetPlatform: GenerationTargetPlatform? = null
}