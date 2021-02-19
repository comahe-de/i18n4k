package de.comahe.i18n4k.messages.provider

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.providers.MessagesProvider
import de.comahe.i18n4k.messages.providers.MessagesProviderFactory
import de.comahe.i18n4k.messages.providers.MessagesProviderViaText
import kotlinx.browser.window
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit

/** Base class for loading the data of a [MessagesProvider] via a lazy loaded list of string */
@Suppress("unused")
class MessagesProviderFactoryViaFetch(
    /** Expected locale. Null for not checking. */
    private val expectedLocale: Locale? = null,
    private val pathToResource: String,
    private val onLoaded: (() -> Unit)? = null,
    private val onFailed: ((Any) -> Unit)? = null
) : MessagesProviderFactory {
    override fun loadMessagesProvider(consumer: (MessagesProvider) -> Unit) {
        val headers = Headers()
        headers.append("encoding", "text/plain;charset='charset=utf-8'")
        window.fetch(
            pathToResource, RequestInit(headers = headers)
        )
            .then { promise -> promise.text() }
            .then { text ->
                try {
                    consumer(MessagesProviderViaText(expectedLocale, text))
                    onLoaded?.invoke()
                } catch (throwable: Throwable) {
                    onFailed?.invoke(throwable)
                }
            }
            .catch { throwable -> onFailed?.invoke(throwable) }

    }
}