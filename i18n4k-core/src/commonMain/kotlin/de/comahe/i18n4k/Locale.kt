package de.comahe.i18n4k

/** Class representing a locale  */
expect class Locale(
    /** See [Locale.getLanguage] */
    language: String,
    /** See [Locale.getCountry] */
    country: String,
    /** See [Locale.getVariant] */
    variant: String
) {
    constructor(
        /** See [Locale.getLanguage] */
        language: String
    )

    constructor(
        /** See [Locale.getLanguage] */
        language: String,
        /** See [Locale.getCountry] */
        country: String
    )


    /** Language code. Should be a [ISO 639-1 code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) */
    fun getLanguage(): String

    /**country/region code for this locale, which should either be the empty string, an uppercase ISO 3166 2-letter code, or a UN M.49 3-digit code. */
    fun getCountry(): String

    /** the variant code for this locale. Can be an empty string */
    fun getVariant(): String

}


/** The current locale of the system or user */
expect val systemLocale: Locale