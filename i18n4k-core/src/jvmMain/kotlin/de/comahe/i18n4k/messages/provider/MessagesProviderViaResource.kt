package de.comahe.i18n4k.messages.provider

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.providers.MessagesProviderViaLoadingText

/** Base class for loading the data of a [MessagesProvider] via a lazy loaded list of string */
@Suppress("unused")
class MessagesProviderViaResource(
    /** Expected locale. Null for not checking. */
    private val expectedLocale: Locale? = null,
    private val pathToResource: String
) : MessagesProviderViaLoadingText(expectedLocale) {
    override fun loadText(): String {
        javaClass.getResourceAsStream(pathToResource)?.use { stream ->
            return String(stream.readAllBytes(), Charsets.UTF_8)
        }
        // resource not found...
        throw IllegalArgumentException("Ressource not found: $pathToResource")
    }

}