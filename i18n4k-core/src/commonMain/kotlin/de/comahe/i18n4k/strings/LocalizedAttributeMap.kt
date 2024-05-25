package de.comahe.i18n4k.strings

import de.comahe.i18n4k.applyLocales

/** A map of attribute values per locale that can be used attribute-based message formatters. */
class LocalizedAttributeMap(

    /** Key: attribute name; Value: map of locale to values */
    private val attributes: Map<String, Map<Locale, String>>
) : LocalizedAttributable {

    /** Uses a list of triples: attribute name, locale, value */
    constructor(vararg attributes: Triple<String, Locale, String>) : this(
        attributes.fold(
            mutableMapOf<String, MutableMap<Locale, String>>()
        ) { map, triple ->
            val localeValues = map.getOrPut(triple.first) { mutableMapOf() }
            localeValues[triple.second] = triple.third
            return@fold map
        }
    )

    override fun getAttribute(attributeName: CharSequence, locale: Locale?): String? {
        val localeValues = attributes[attributeName] ?: return null
        return applyLocales(locale) { localeValues[it] }
    }
}