package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale

/** For objects that change their [toString] method based on the set [Locale] in [i18n4k] */
interface LocalizedString {

    /** return value based on the currently set [Locale] in [i18n4k] ([I18n4kConfig.locale]) */
    override fun toString(): String

    /**
     * return value based on the given [Locale]
     *
     * @param locale the [Locale] to be use. If null the current setting in [i18n4k]
     *     ([I18n4kConfig.locale]) will be used.
     */
    fun toString(locale: Locale?): String

    /** Shortcut for [toString] */
    operator fun invoke() =
        toString(null)

    /** Shortcut for [toString] */
    operator fun invoke(locale: Locale?) =
        toString(locale)

    /**
     * Returns the value of the attribute with the given name or null if the attribute value is not
     * defined.
     */
    fun getAttribute(attributeName: CharSequence, locale: Locale? = null): String? = null
}

/**
 * Return the string value based on the given [Locale].
 *
 * If this is not a [LocalizedString] the notmal [toString] will be called.
 *
 * @param locale the [Locale] to be use. If null the current setting in [i18n4k]
 *     ([I18n4kConfig.locale]) will be used.
 */
fun Any?.toString(locale: Locale?): String {
    if (this is LocalizedString)
        return this.toString(locale)
    return this.toString()
}