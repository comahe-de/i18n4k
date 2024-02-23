package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageParameters
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MessagePartList(val list: ImmutableList<MessagePart>) : MessagePart {

    constructor(part: MessagePart) : this(persistentListOf(part))
    constructor(vararg parts: MessagePart) : this(persistentListOf(*parts))

    override fun fillInParameterNames(names: MutableSet<CharSequence>) {
        for (part in list)
            part.fillInParameterNames(names)
    }

    override fun format(
        result: StringBuilder,
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    ) {
        for (part in list)
            part.format(result, parameters, locale, context)
    }


}
