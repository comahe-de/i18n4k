package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MessagePartList(val list: ImmutableList<MessagePart>) : MessagePart {

    constructor(part: MessagePart) : this(persistentListOf(part))
    constructor(vararg parts: MessagePart) : this(persistentListOf(*parts))

    override val maxParameterIndex: Int
        get() =
            if (hasNamedParameters)
                -1
            else
                list.maxOf { it.maxParameterIndex }

    override val hasNamedParameters: Boolean
        get() = list.firstOrNull() { it.hasNamedParameters } != null

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
