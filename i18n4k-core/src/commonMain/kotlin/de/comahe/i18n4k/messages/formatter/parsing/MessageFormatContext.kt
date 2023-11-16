package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import kotlinx.collections.immutable.ImmutableMap

data class MessageFormatContext(
    val formatterTypes : ImmutableMap<String, MessageValueFormatter>
) {

}