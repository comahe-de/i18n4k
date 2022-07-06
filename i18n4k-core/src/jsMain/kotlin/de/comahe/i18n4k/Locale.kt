package de.comahe.i18n4k

import kotlinx.browser.window

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

actual val systemLocale: Locale =
    forLocaleTag(window.navigator.language,"-")