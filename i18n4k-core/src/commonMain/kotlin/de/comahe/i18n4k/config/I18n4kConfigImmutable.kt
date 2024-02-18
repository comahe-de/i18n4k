package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProvider
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProviderDefault
import de.comahe.i18n4k.messages.formatter.provider.GenderProvider
import de.comahe.i18n4k.messages.formatter.provider.GenderProviderDefault
import de.comahe.i18n4k.systemLocale

/**
 * Implementation of [I18n4kConfig] that is immutable (for state engines like React).
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
    override val ignoreMessageParseErrors: Boolean,
    override val genderProvider: GenderProvider,
    override val declensionProvider: DeclensionProvider,
) : I18n4kConfig {
    /** constructor for getting the default settings */
    constructor() : this(
        defaultLocale = Locale("en"),
        locale = systemLocale,
        messageFormatter = MessageFormatterDefault,
        treadBlankStringAsNull = true,
        ignoreMessageParseErrors = true,
        genderProvider = GenderProviderDefault,
        declensionProvider = DeclensionProviderDefault,
    )

    fun withLocaleTag(languageCodeNew: String) = withLocale(forLocaleTag(languageCodeNew))

    fun withLocale(localeNew: Locale) = copy(locale = localeNew)
}