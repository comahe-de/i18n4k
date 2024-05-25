package de.comahe.i18n4k.messages.providers

/** Provides traslations for a message bundle for a given locale */
interface MessagesProvider {
    /** which locale does the provider use*/
    val locale: Locale

    /** count of strings in this provider */
    val size: Int

    /** get message at index */
    operator fun get(index: Int): String?
}