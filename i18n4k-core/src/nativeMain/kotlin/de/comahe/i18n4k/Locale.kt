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

// TODO how to get the system locale in Kotlin-Native?
actual val systemLocale: Locale
    get() {
        return Locale("en")
    }