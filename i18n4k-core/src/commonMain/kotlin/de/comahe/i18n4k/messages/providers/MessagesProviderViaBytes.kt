package de.comahe.i18n4k.messages.providers

import de.comahe.i18n4k.Locale

/** Load a text string as from byte array
 *
 * Text must be formatted as described in [MessagesProviderViaLoadingText] */
@Suppress("unused")
class MessagesProviderViaBytes(
    /** Expected locale. Null for not checking. */
    private val expectedLocale: Locale? = null,
    private val textBytes: ByteArray
) : MessagesProviderViaLoadingText(expectedLocale) {

    override fun loadText(): String = textBytes.decodeToString()
}