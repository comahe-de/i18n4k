package de.comahe.i18n4k.messages.providers

/** Creates a [MessagesProvider] via callback/asynchronously  */
interface MessagesProviderFactory {
    /** Creates a [MessagesProvider] asynchronously  */
    fun loadMessagesProvider(consumer: (MessagesProvider) -> Unit)
}