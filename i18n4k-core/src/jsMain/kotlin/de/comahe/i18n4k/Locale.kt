package de.comahe.i18n4k

actual data class Locale actual constructor(
    val language: String,
    val country: String,
    val variant: String
) {
    actual constructor(
        language: String
    ) : this(language, "", "")

    actual constructor(
        language: String,
        country: String
    ) : this(language, country, "")

    actual fun getLanguage(): String = language
    actual fun getCountry(): String = country
    actual fun getVariant(): String = variant

    override fun toString(): String = toTag()
}

actual val systemLocale: Locale = run {
    var locale: Locale? = null
    try {
        if (jsTypeOf(kotlinx.browser.window) != "undefined")
            locale = forLocaleTag(kotlinx.browser.window.navigator.language, "-")
    } catch (ignore: Throwable) {
    }
    if (locale == null) {
        locale = Locale("en")
    }
    return@run locale
}