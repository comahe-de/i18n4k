package de.comahe.i18n4k


/** Class representing a locale */
expect class Locale {
    @Deprecated(
        message = "Use `createLocale`",
        replaceWith = ReplaceWith("createLocale(language)"),
        level = DeprecationLevel.WARNING
    )
    constructor(
        /** See [Locale.getLanguage] */
        language: String
    )

    @Deprecated(
        message = "Use `createLocale`",
        replaceWith = ReplaceWith("createLocale(language, null, country)"),
        level = DeprecationLevel.WARNING
    )
    constructor(
        /** See [Locale.getLanguage] */
        language: String,
        /** See [Locale.getCountry] */
        country: String
    )

    @Deprecated(
        message = "Use `createLocale`",
        replaceWith = ReplaceWith("createLocale(language, null, country, variant)"),
        level = DeprecationLevel.WARNING
    )
    constructor  (
        /** See [Locale.getLanguage] */
        language: String,
        /** See [Locale.getCountry] */
        country: String,
        /** See [Locale.getVariant] */
        variant: String
    )


    /**
     * Language code.
     *
     * Should be a [ISO 639-1 code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes).
     *
     * Will be lowercase.
     */
    fun getLanguage(): String

    /**
     * Returns the script for this locale, which should either be the empty string or an "ISO 15924
     * 4"-letter script code.
     *
     * The first letter is uppercase and the rest are lowercase, for example, 'Latn', 'Cyrl'.
     */
    fun getScript(): String

    /**
     * country/region code for this locale, which should either be the empty string, an uppercase
     * ISO 3166 2-letter code, or a UN M.49 3-digit code.
     */
    fun getCountry(): String

    /** the variant code for this locale. Can be an empty string */
    fun getVariant(): String

    /** Returns `true` if this `Locale` has any extensions. */
    fun hasExtensions(): Boolean

    /**
     * Returns a copy of this (or this itself) `Locale` with no extensions.
     *
     * If this `Locale` has no extensions, this `Locale` is returned.
     */
    fun stripExtensions(): Locale

    /**
     * Returns the extension (or private use) value associated with the specified key, or null if
     * there is no extension associated with the key.
     *
     * To be well-formed, the key must be one of `[0-9A-Za-z]`.
     *
     * Keys are case-insensitive, so for example, 'z' and 'Z' represent the same extension.
     *
     * @param key the extension key
     * @return The extension, or null if this locale defines no extension for the specified key.
     * @throws IllegalArgumentException if key is not well-formed
     */
    fun getExtension(key: Char): String?

    /**
     * Returns the set of extension keys associated with this locale, or the empty set if it has no
     * extensions.
     *
     * The returned set is unmodifiable.
     *
     * The keys will all be lower-case.
     */
    fun getExtensionKeys(): Set<Char>
}

/**
 * Create a local with extensions.
 *
 * Regarding extensions see:
 * * [language-tags](https://www.w3.org/International/articles/language-tags/#extension)
 * * [rfc5646](https://www.rfc-editor.org/rfc/rfc5646.html#section-2.2.6)
 */
expect fun createLocale(
    language: String,
    script: String? = null,
    country: String? = null,
    variant: String? = null,
    extensions: Map<Char, String>? = null,
): Locale

/** The current locale of the system or user */
expect val systemLocale: Locale

