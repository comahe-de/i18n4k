package de.comahe.i18n4k.strings

/** [LocalizedString] that always return the same [value] in [toString] */
class SimpleLocalizedString(private val value: Any) : LocalizedString {

    override fun toString(): String = value.toString()

    override fun toString(locale: Locale?) = value.toString()
}