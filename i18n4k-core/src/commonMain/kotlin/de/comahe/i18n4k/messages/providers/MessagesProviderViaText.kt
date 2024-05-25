package de.comahe.i18n4k.messages.providers

/** Load a text string.
 *
 * Text must be formatted as described in [MessagesProviderViaLoadingText] */
@Suppress("unused")
class MessagesProviderViaText(
    /** Expected locale. Null for not checking. */
    private val expectedLocale: Locale? = null,
    private val text: String
) : MessagesProviderViaLoadingText(expectedLocale) {

    override fun loadText(): String = text
}