package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale

/** Simple implementation of [LocalizedAttributable] that always return the given value */
class SimpleLocalizedAttributable(private val value: Any? = null) : LocalizedAttributable {
    override fun getAttribute(attributeName: CharSequence, locale: Locale?): String? =
        value?.toString()
}