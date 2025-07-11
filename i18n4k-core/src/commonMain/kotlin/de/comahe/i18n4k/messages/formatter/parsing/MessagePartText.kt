package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageParameters

data class MessagePartText(val text: CharSequence) : MessagePart {

    override fun fillInParameterNames(names: MutableList<Pair<CharSequence, CharSequence?>>) {
    }

    override fun format(
        result: StringBuilder,
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    ) {
        result.append(text)
    }

    override fun format(
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    ): CharSequence {
        return text
    }


}
