package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale

/** A combination of two [LocalizedString] forming a new one */
class CompositeLocalizedString(
    private val string1: LocalizedString,
    private val string2: LocalizedString
) : LocalizedString {
    override fun toString(): String {
        return string1.toString() + string2.toString()
    }

    override fun toString(locale: Locale?): String {
        return string1.toString(locale) + string2.toString(locale)
    }
}

/** Combines the two [LocalizedString] into one */
operator fun LocalizedString.plus(other: LocalizedString): LocalizedString =
    CompositeLocalizedString(this, other)

/** Combines the [LocalizedString] and the [String] into a [LocalizedString] */
operator fun LocalizedString.plus(other: String): LocalizedString =
    CompositeLocalizedString(this, SimpleLocalizedString(other))