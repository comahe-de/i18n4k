package de.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfig
import de.comahe.i18n4k.strings.LocalizedString


val String.asLocalizedString: LocalizedString
    get() = object : LocalizedString {
        override fun toString(): String = toString(null)
        override fun toString(locale: Locale?): String = this@asLocalizedString
    }

val String.toLocalize: LocalizedString
    get() = asLocalizedString

class ToLocalizedString(val string: String, vararg val args: Any) : LocalizedString {
    constructor() : this("")

    override fun toString(): String = toString(null)
    override fun toString(locale: Locale?): String = when {
        args.isEmpty() -> string
        else -> String.format(string, *args)
    }
}