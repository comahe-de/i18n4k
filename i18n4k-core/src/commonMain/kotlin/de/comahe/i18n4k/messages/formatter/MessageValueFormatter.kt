package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.parsing.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.parsing.StylePart

/**
 * Format special types of values
 *
 * Used by [MessageFormatterDefault]
 */
interface MessageValueFormatter {

    /** ID of the type of the formatter */
    val typeId: String

    /** Formats the given [value] and writes is to [result]. */
    fun format(
        result: StringBuilder,
        value: Any?,
        style: StylePart?,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext,
    )
}