package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale

data class MessagePartText(val text: CharSequence) : MessagePart {

    override val maxParameterIndex: Int
        get() = -1

    override fun format(
        result: StringBuilder,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ) {
        result.append(text)
    }

    override fun format(
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ): CharSequence {
        return text
    }


}
