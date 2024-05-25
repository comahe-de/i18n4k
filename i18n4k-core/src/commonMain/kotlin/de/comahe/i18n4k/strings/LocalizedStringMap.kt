package de.comahe.i18n4k.strings

import de.comahe.i18n4k.applyLocales

/**
 * A simple implementation of [LocalizedString] that maps a locale values to string values.
 *
 * Attributes may also be specified with the `attributes` parameter.
 */
class LocalizedStringMap(
    private val messages: Map<Locale, String>,
    private val attributes: LocalizedAttributable?
) : LocalizedString, LocalizedAttributable {

    constructor(
        vararg messages: Pair<Locale, String>,
        attributes: LocalizedAttributable? = null
    ) :
        this(
            messages = messages.fold(mutableMapOf<Locale, String>()) { map, pair ->
                map[pair.first] = pair.second
                return@fold map
            },
            attributes = attributes
        )

    override fun getAttribute(attributeName: CharSequence, locale: Locale?): String? {
        return attributes?.getAttribute(attributeName, locale)
    }

    override fun toString(): String = toString(null)


    override fun toString(locale: Locale?): String =
        applyLocales(locale) { messages[it] } ?: "?"
}