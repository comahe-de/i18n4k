package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.collections.immutable.toPersistentMap

data class MessageFormatContext(
    val formatterTypes: ImmutableMap<CharSequence, MessageValueFormatter>,
    val formatterPrefixTypes: ImmutableSet<MessageValueFormatter>,
) {
    constructor(formatters: Collection<MessageValueFormatter>) :
        this(
            @Suppress("USELESS_CAST")
            formatters.filter { !it.typeIdIsPrefix }
                .associateBy({ it.typeId as CharSequence }, { it })
                .toPersistentMap(),
            formatters.filter { it.typeIdIsPrefix }.toImmutableSet(),
        )

    fun getFormatterFor(typeId: CharSequence): MessageValueFormatter? {
        return formatterTypes[typeId]
            ?: formatterPrefixTypes.firstOrNull { typeId.startsWith(it.typeId) }
    }

    /** Creates a new instance where the formatter was added */
    fun withMessageValueFormatters(vararg f: MessageValueFormatter): MessageFormatContext {
        return MessageFormatContext(formatterTypes.values + formatterPrefixTypes + f)
    }
}