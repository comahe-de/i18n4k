package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatter

/** Global configuration interface for all the I18N parameters */
interface I18n4kConfig {

    /** Fallback locale when a string in the currently set [locale] are not found */
    val defaultLocale: Locale

    /** currently set locale */
    val locale: Locale

    /** the format of the parameters */
    val messageFormatter: MessageFormatter

    /**
     * if a string of a localisation is null, the default locale is used. If this flag is true,
     * blank strings (empty of only whitespace) are also threaded as null.
     *
     *  For null strings the default locale is used.
     */
    val treadBlankStringAsNull: Boolean
}