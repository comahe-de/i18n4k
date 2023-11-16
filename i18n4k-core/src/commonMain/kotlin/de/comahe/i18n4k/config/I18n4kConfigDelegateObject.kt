package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatter

/**
 * Implementation of [I18n4kConfig] that delegates request to the
 * provider object
 */
@Suppress("unused")
class I18n4kConfigDelegateObject(
    @Suppress("MemberVisibilityCanBePrivate") var configProvider: I18n4kConfig
) : I18n4kConfig {

    override val defaultLocale: Locale
        get() = configProvider.defaultLocale

    override val locale: Locale
        get() = configProvider.locale

    override val messageFormatter: MessageFormatter
        get() = configProvider.messageFormatter

    override val treadBlankStringAsNull: Boolean
        get() = configProvider.treadBlankStringAsNull

    override val ignoreMessageParseErrors: Boolean
        get() = configProvider.ignoreMessageParseErrors
}