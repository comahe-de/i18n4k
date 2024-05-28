package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale

/** For objects that change their [toString] method based on the set [Locale] in [de.comahe.i18n4k.i18n4k] */
interface LocalizedString {

    /**
     * Returns the string value based on the currently set [Locale] in [de.comahe.i18n4k.i18n4k]
     * ([de.comahe.i18n4k.config.I18n4kConfig.locale])
     *
     * Also, less specific locales and the default locale were used to find a string value if no
     * value is available for the given locale.
     */
    override fun toString(): String

    /**
     * Returns the string value based on the given [Locale].
     *
     * Also, less specific locales and the default locale were used to find a string value if no
     * value is available for the given locale.
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