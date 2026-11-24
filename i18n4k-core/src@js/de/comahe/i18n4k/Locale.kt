package de.comahe.i18n4k

actual typealias Locale = DefaultLocaleImpl

actual fun createLocale(
    language: String,
    script: String?,
    country: String?,
    variant: String?,
    extensions: Map<Char, String>?
) = createDefaultLocaleImpl(language, script, country, variant, extensions)

actual val systemLocale: Locale = run {
    var locale: Locale? = null
    try {
        if (jsTypeOf(kotlinx.browser.window) != "undefined")
            locale = forLocaleTag(kotlinx.browser.window.navigator.language)
    } catch (ignore: Throwable) {
    }
    if (locale == null) {
        locale = Locale("en")
    }
    return@run locale
}