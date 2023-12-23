package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext

sealed interface MessagePart {

    /**
     * returns the max used parameter index in the message. "-1" if there is no
     * parameter
     */
    val maxParameterIndex: Int

    fun format(
        result: StringBuilder,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    )

    fun format(
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ): CharSequence {
        val builder = StringBuilder()
        format(builder, parameters, locale, context)
        return builder
    }


}
