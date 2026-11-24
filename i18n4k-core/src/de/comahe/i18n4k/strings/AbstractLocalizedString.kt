package de.comahe.i18n4k.strings

/** Common base class for implementing [LocalizedString] */
abstract class AbstractLocalizedString : LocalizedString {
    override fun toString() =
        toString(null)

}