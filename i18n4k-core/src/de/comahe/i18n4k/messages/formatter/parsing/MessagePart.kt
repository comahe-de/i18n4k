package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageParameters

sealed interface MessagePart : TextWithParameters {

    fun format(
        result: StringBuilder,
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    )

    fun format(
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    ): CharSequence {
        val builder = StringBuilder()
        format(builder, parameters, locale, context)
        return builder
    }


}
