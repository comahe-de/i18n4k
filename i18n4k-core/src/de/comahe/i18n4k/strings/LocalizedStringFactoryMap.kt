package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.applyLocales
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.MessageParameters

/**
 * A simple implementation of [LocalizedStringFactoryN] that maps a locale values to string values.
 *
 * Attributes may also be specified with the `attributes` parameter.
 */
class LocalizedStringFactoryMap(
    private val messages: Map<Locale, String>,
    private val attributes: LocalizedAttributable?
) : LocalizedStringFactoryN {

    constructor(vararg messages: Pair<Locale, String>, attributes: LocalizedAttributable? = null) :
        this(
            messages = messages.fold(mutableMapOf<Locale, String>()) { map, pair ->
                map[pair.first] = pair.second
                return@fold map
            },
            attributes = attributes
        )

    override fun createString(parameters: MessageParameters, locale: Locale?): String {
        val message = applyLocales(locale) { messages[it] } ?: return "?"

        return i18n4k.messageFormatter.format(
            message,
            parameters,
            locale ?: i18n4k.locale
        )
    }

    override fun createLocalizedString(parameters: MessageParameters): LocalizedString {
        return object : LocalizedString, LocalizedAttributable {

            override fun toString(): String {
                return this@LocalizedStringFactoryMap.createString(parameters)
            }

            override fun toString(locale: Locale?): String {
                return this@LocalizedStringFactoryMap.createString(parameters, locale)
            }

            override fun getAttribute(attributeName: CharSequence, locale: Locale?): String? {
                return attributes?.getAttribute(attributeName, locale)
            }
        }
    }

}