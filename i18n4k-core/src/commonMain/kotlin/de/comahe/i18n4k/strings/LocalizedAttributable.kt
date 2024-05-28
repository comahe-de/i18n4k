package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale

/** For objects that hold different attribute values for different [Locale] values */
interface LocalizedAttributable {

    /**
     * Returns the value of the attribute with the given name in the given locale or null if the
     * attribute value is not defined for the locale.
     *
     * Also, less specific locales and the default locale were used to find an attribute value if no
     * value is available for the given locale.
     *
     * @param attributeName Name of the attribute
     * @param locale Used [Locale] to look up the attribute value. If null, the [Locale] in
     *     [de.comahe.i18n4k.i18n4k] ([de.comahe.i18n4k.config.I18n4kConfig.locale]) is used.
     */
    fun getAttribute(attributeName: CharSequence, locale: Locale? = null): String?
}
