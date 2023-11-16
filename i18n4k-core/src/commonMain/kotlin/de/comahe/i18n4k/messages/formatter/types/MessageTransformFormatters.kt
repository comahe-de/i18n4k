package de.comahe.i18n4k.messages.formatter.types

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import de.comahe.i18n4k.messages.formatter.parsing.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.parsing.StylePart
import de.comahe.i18n4k.messages.formatter.parsing.toSimple
import kotlinx.collections.immutable.persistentListOf

/**
 * Provides the possibility to transform the letters to uppercase,
 * lowercase or capitalize (uppercase the first letter, rest lowercase).
 *
 * Format:
 *
 *     {~, FORMAT_OPTION, TEXT_WITH_PATTERNS}
 * - FORMAT_OPTION
 *    - uppercase: transform all letters to uppercase
 *    - lowercase: transform all letters to lowercase
 *    - capitalize: transform the first letter to uppercase
 * - TEXT_WITH_PATTERNS: Any text including patterns.
 *
 * Example:
 *
 *     {~, capitalize, {0} } is the best!
 *
 * Result for "ice": Ice is the best!
 */
object MessageTransformFormatters {
    val all = persistentListOf(
        UppercaseFormatter, LowercaseFormatter, CapitalizeFormatter
    )

    abstract class TransformFormatter(override val typeId: String) : MessageValueFormatter {

        override fun format(
            result: StringBuilder,
            value: Any?,
            style: StylePart?,
            parameters: List<Any>,
            locale: Locale,
            context: MessageFormatContext,
        ) {
            val textToTransform = style?.toSimple()?.format(parameters, locale, context)
                ?: return
            result.ensureCapacity(result.length + textToTransform.length)
            formatString(result, textToTransform)
        }

        protected abstract fun formatString(
            result: StringBuilder,
            value: CharSequence,
        )
    }

    object UppercaseFormatter : TransformFormatter("uppercase") {
        override fun formatString(
            result: StringBuilder,
            value: CharSequence,
        ) {
            for (c in value)
                result.append(c.uppercaseChar())
        }
    }

    object LowercaseFormatter : TransformFormatter("lowercase") {
        override fun formatString(
            result: StringBuilder,
            value: CharSequence,
        ) {
            for (c in value)
                result.append(c.lowercaseChar())
        }
    }

    object CapitalizeFormatter : TransformFormatter("capitalize") {
        override fun formatString(
            result: StringBuilder,
            value: CharSequence,
        ) {
            if (value.isEmpty())
                return
            result.append(value[0].uppercaseChar())
            for (i in 1 until value.length)
                result.append(value[i])
        }
    }
}