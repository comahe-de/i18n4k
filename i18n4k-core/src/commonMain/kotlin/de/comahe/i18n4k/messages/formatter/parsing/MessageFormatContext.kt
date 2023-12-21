package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentMap

data class MessageFormatContext(
    val formatterTypes: ImmutableMap<CharSequence, MessageValueFormatter>,
    val formatterPrefixTypes: ImmutableList<MessageValueFormatter>,
) {
    constructor(formatters: Collection<MessageValueFormatter>) :
        this(
            @Suppress("USELESS_CAST")
            formatters.filter { !it.typeIdIsPrefix }
                .associateBy({ it.typeId as CharSequence }, { it })
                .toPersistentMap(),
            formatters.filter { it.typeIdIsPrefix }.toImmutableList(),
        )

    fun getFormatterFor(typeId: CharSequence): MessageValueFormatter? {
        return formatterTypes[typeId]
            ?: formatterPrefixTypes.firstOrNull { typeId.startsWith(it.typeId) }
    }
}