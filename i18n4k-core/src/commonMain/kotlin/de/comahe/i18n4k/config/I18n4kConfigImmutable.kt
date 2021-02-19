package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.systemLocale

/**  Implementation of [I18n4kConfig] that is immutable (for state engines like React).
 *
 * Can be used in combination with [I18n4kConfigDelegate], which should return the current config
 * from the state.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
data class I18n4kConfigImmutable(
    override val defaultLocale: Locale,
    override val locale: Locale,
    override val messageFormatter: MessageFormatter,
    override val treadBlankStringAsNull: Boolean,
) : I18n4kConfig {
    /** constructor for getting the default settings */
    constructor() : this(
        defaultLocale = Locale("en"),
        locale = systemLocale,
        messageFormatter = MessageFormatterDefault,
        treadBlankStringAsNull = true
    )

    fun withLocaleTag(languageCodeNew: String) = withLocale(forLocaleTag(languageCodeNew))

    fun withLocale(localeNew: Locale) = I18n4kConfigImmutable(
        defaultLocale = defaultLocale,
        locale = localeNew,
        messageFormatter = messageFormatter,
        treadBlankStringAsNull = treadBlankStringAsNull
    )
}