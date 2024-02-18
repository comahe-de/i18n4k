package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProvider
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProviderDefault
import de.comahe.i18n4k.messages.formatter.provider.GenderProvider
import de.comahe.i18n4k.messages.formatter.provider.GenderProviderDefault

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
     * For null strings the default locale is used.
     */
    val treadBlankStringAsNull: Boolean

    /**
     * if true, error during parsing of messages will be ignored and the message will be printed
     * without the invalid part.
     */
    val ignoreMessageParseErrors: Boolean
        get() = true

    /** Provider used for gender requests */
    val genderProvider: GenderProvider
        get() = GenderProviderDefault

    /** Provider used for declension requests */
    val declensionProvider: DeclensionProvider
        get() = DeclensionProviderDefault
}