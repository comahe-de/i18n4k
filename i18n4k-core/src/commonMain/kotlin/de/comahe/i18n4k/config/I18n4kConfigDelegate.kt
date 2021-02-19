package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatter

/**  Implementation of [I18n4kConfig] that request the config from a provider function and delegates
 * request to the returned value */
@Suppress("unused")
class I18n4kConfigDelegate(val configProvider: () -> I18n4kConfig) : I18n4kConfig {

    override val defaultLocale: Locale
        get() = configProvider().defaultLocale

    override val locale: Locale
        get() = configProvider().locale

    override val messageFormatter: MessageFormatter
        get() = configProvider().messageFormatter

    override val treadBlankStringAsNull: Boolean
        get() = configProvider().treadBlankStringAsNull
}