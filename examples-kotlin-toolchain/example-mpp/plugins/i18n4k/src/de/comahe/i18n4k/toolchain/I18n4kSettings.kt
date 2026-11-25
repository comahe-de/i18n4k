package de.comahe.i18n4k.toolchain

import org.jetbrains.amper.plugins.Configurable

/**
 * Configuration of the i18n4k code generation for one module.
 *
 * Enable and configure the plugin in the module's `module.yaml`:
 *
 * ```yaml
 * plugins:
 *   i18n4k:
 *     enabled: true
 *     sourceCodeLocales: [ en, de ]
 * ```
 *
 * Every property has a default, so `i18n4k: enabled` is enough for the common
 * case.
 */
@Configurable
interface I18n4kSettings {

    /**
     * Directory with the message bundle files (Java properties files), relative
     * to the module root.
     *
     * Every subdirectory becomes a package in the generated Kotlin code.
     *
     * Message bundles are build inputs, not sources, so the default is `i18n`
     * at the module root (next to `resources` / `composeResources`). Projects
     * with `layout: maven-like` usually use `src/main/i18n`.
     */
    val inputDirectory: String get() = "i18n"

    /**
     * Package name of the generated classes.
     *
     * If `null` (the default), the path of the bundle file relative to
     * [inputDirectory] is used as package name.
     */
    val packageName: String?

    /**
     * Locale tag whose message texts are added as KDoc comments to the
     * generated properties. `null` disables the comments.
     */
    val commentLocale: String? get() = "en"

    /**
     * Locale tags whose translations are generated *into the Kotlin code*
     * instead of into resource files.
     *
     * Inline translations do not need resource loading at runtime.
     *
     * `null` (the default) means "all locales"; an empty list means "no locale".
     */
    val sourceCodeLocales: List<String>?

    /**
     * Target platform of the generated code.
     *
     * The default fits Kotlin Multiplatform common code and also works for
     * JVM-only modules. Set [GenerationTarget.ANDROID] for Android modules,
     * which switches the generated language files to the raw-resource layout.
     */
    val generationTarget: GenerationTarget get() = GenerationTarget.MULTI_PLATFORM

    /**
     * Enable generic `LocalizedString` factories for messages that declare
     * value types (`You have {0:int} unread messages`).
     */
    val valueTypesEnabled: Boolean get() = true

    /**
     * Mapping of value type names used in message files to fully qualified
     * class names. Only evaluated if [valueTypesEnabled] is `true`.
     */
    val valueTypeMapping: Map<String, String> get() = emptyMap()

    /**
     * Add the `@JsExport` annotation to the generated message bundle objects.
     *
     * Experimental, only relevant for [GenerationTarget.JS] and
     * [GenerationTarget.MULTI_PLATFORM].
     */
    val enableJsExportAnnotation: Boolean get() = false
}
