package de.comahe.i18n4k

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.variantCode

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

actual val systemLocale: Locale
    get() {
        val locale = NSLocale.currentLocale
        return Locale(
            language = locale.languageCode,
            country = locale.countryCode.orEmpty(),
            variant = locale.variantCode.orEmpty()
        )
    }