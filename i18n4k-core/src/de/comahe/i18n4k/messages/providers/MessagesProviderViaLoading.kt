package de.comahe.i18n4k.messages.providers

import de.comahe.i18n4k.Locale

/** Base class for loading the data of a [MessagesProvider] via a lazy loaded list of string */
@Suppress("unused")
abstract class MessagesProviderViaLoading(
) : MessagesProvider {

    /** lazy loaded list of strings */
    private val data: Pair<Locale, List<String?>> by lazy {
        load()
    }

    override val locale: Locale
        get() = data.first

    override fun get(index: Int): String? =
        data.second[index]

    override val size: Int
        get() = data.second.size

    /** Load the messages. Will only be called once! */
    protected abstract fun load(): Pair<Locale, List<String?>>
}