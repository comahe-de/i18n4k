package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale

/** [LocalizedString] that always returns "" in [toString] */
object EmptyLocalizedString : LocalizedString {

    override fun toString(): String = ""

    override fun toString(locale: Locale?) = ""
}