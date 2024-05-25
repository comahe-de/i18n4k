package de.comahe.i18n4k.messages.providers

/** A [MessagesProvider] that uses a simple list */
@Suppress("unused")
class MessagesProviderViaList(
    override val locale: Locale,
    /** List of strings in this [MessagesProvider]. See [get]. */
    private val list: List<String?>
) : MessagesProvider {
    override val size: Int
        get() = list.size

    override fun get(index: Int): String? = list[index]
}