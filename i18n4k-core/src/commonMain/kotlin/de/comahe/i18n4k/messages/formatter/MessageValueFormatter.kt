package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.parsing.StylePart

/**
 * Format special types of values
 *
 * Used by [MessageFormatterDefault]
 */
interface MessageValueFormatter {

    /** ID of the type of the formatter */
    val typeId: String

    /**
     * True if [typeId] is just a prefix for the type.
     *
     * The suffix is handled by the formatter.
     */
    val typeIdIsPrefix: Boolean
        get() = false

    /** Formats the given [value] and writes is to [result]. */
    fun format(
        result: StringBuilder,
        value: Any?,
        typeId: CharSequence,
        style: StylePart?,
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext,
    )
}