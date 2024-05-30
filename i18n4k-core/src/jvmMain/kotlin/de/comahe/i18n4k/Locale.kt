package de.comahe.i18n4k

import de.comahe.i18n4k.strings.capitalize


actual typealias Locale = java.util.Locale

actual fun createLocale(
    language: String,
    script: String?,
    country: String?,
    variant: String?,
    extensions: Map<Char, String>?
): Locale {
    val builder = java.util.Locale.Builder()

    builder.setLanguage(language.lowercase())
    if (script != null)
        builder.setScript(script.capitalize())
    if (country != null)
        builder.setRegion(country.uppercase())
    if (variant != null)
        builder.setVariant(variant.lowercase().take(8))
    extensions?.forEach { (key, value) ->
        builder.setExtension(key.lowercaseChar(), value.lowercase())
    }
    return builder.build()
}

actual val systemLocale: Locale
    get() {
        val userLanguage = System.getProperty("user.language")
            ?: return Locale.getDefault()
        return java.util.Locale(userLanguage)
    }