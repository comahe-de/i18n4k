package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import kotlinx.collections.immutable.ImmutableList

data class MessagePartList(val list: ImmutableList<MessagePart>) : MessagePart {

    override val maxParameterIndex: Int
        get() = list.maxOf { it.maxParameterIndex }

    override fun format(
        result: StringBuilder,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ) {
        for (part in list)
            part.format(result, parameters, locale, context)
    }


}
