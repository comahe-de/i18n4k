package de.comahe.i18n4k.strings

/** [LocalizedString] that always returns "" in [toString] */
object EmptyLocalizedString : LocalizedString {

    override fun toString(): String = ""

    override fun toString(locale: Locale?) = ""
}