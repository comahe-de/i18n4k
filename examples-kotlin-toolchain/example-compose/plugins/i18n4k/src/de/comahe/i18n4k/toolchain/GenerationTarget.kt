package de.comahe.i18n4k.toolchain

/**
 * The platform the code is generated for.
 *
 * This enum mirrors [de.comahe.i18n4k.generator.GenerationTargetPlatform].
 * Plugin configuration may only use enums declared in the plugin module itself,
 * so the generator enum cannot be used directly in [I18n4kSettings].
 */
enum class GenerationTarget {
    /** JVM only. */
    JVM,

    /**
     * Android. Like the Gradle plugin, the generated language files are written
     * in the "raw Android resource" style (flat files, lower-case snake_case
     * names) below the generated resources directory.
     */
    ANDROID,

    /** Kotlin/JS only. */
    JS,

    /** Kotlin/Native only. */
    NATIVE,

    /**
     * Code shared between several platforms (Kotlin Multiplatform).
     *
     * This is the default: the generated code is plain common Kotlin, with
     * `@JvmField` (ignored on non-JVM platforms) and, if enabled, `@JsExport`.
     */
    MULTI_PLATFORM,
}
