package de.comahe.i18n4k.messages.formatter.types

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import de.comahe.i18n4k.messages.formatter.parsing.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.parsing.StylePart
import de.comahe.i18n4k.messages.formatter.parsing.toSimple
import de.comahe.i18n4k.strings.LocalizedString
import de.comahe.i18n4k.strings.LocalizedStringNumber
import de.comahe.i18n4k.strings.isDigit
import kotlinx.collections.immutable.persistentListOf

/**
 * A [MessageValueFormatter] for numbers ([Number])
 *
 * Format:
 *
 *     {~, FORMAT_TYPE, FORMAT_STYLE}
 *
 * Supported values for `FORMAT_TYPE` (see sub objects)
 * * number
 * * length
 * * area
 * * weight
 * * timespan
 *
 * Supported values for `FORMAT_STYLE`
 * * "%.0P":
 *    * P: precision: If given, the position to which the rounding is to be
 *      made. Positive for after the decimal point, negative for before the
 *      decimal point, 0 for without decimal places.
 *    * 0: Tailing zeros: If given and `P` is given an positive, `P` is the
 *      exact number digits after the fraction sign. (zeros are appended if
 *      necessary)
 *
 * Usage examples for number = 12345.6789:
 * * `{0,number}` - Format parameter 0 as number, e.g. "12,345.6789"
 * * `{0,number, %.2}` - Format parameter 0 as number with max. 2 fraction
 *   digits, e.g. "12,345.68"
 * * `{0,number, %.06}` - Format parameter 0 as number with exact. 6
 *   fraction digits, e.g. "12,345.678900"
 * * `{0,length}` - Format parameter 0 as length (in meters), e.g.
 *   "12.3456789 km"
 * * `{0,length, %.2}` - Format parameter 0 as length (in meters) with 2
 *   fraction digits, e.g. "12.35 km"
 * * `{0,area}` - Format parameter 0 as area (in m²), e.g. "12,345.6789 m²"
 * * `{0,area, %.2}` - Format parameter 0 as area (in m²) with 2 fraction
 *   digits, e.g. "12,345.68 m²"
 * * `{0,weight}` - Format parameter 0 as weight (in grams), e.g.
 *   "12.3456789 kg"
 * * `{0,weight, %.2}` - Format parameter 0 as weight (in grams) with 2
 *   fraction digits, e.g. "12.35 kg"
 * * `{0,timespan}` - Format parameter 0 as timespan (in seconds), e.g.
 *   "3:25:45 h"
 */
object MessageNumberFormatters {
    //////////////////////

    val all = persistentListOf(
        NumberFormatter,
        LengthFormatter,
        AreaFormatter,
        WeightFormatter,
        TimespanFormatter
    )

    abstract class BaseNumberFormatter(override val typeId: String) : MessageValueFormatter {

        override fun format(
            result: StringBuilder,
            value: Any?,
            style: StylePart?,
            parameters: List<Any>,
            locale: Locale,
            context: MessageFormatContext,
        ) {


            if (value !is Number) {
                if (value is LocalizedString)
                    result.append(value.toString(locale))
                else
                    result.append(value.toString())
                return
            }

            val formatStyle = style?.toSimple()?.format(parameters, locale, context)

            result.append(
                formatNumber(
                    value,
                    parsePrecisionFromFormatStyle(formatStyle),
                    parseTailingFractionZerosFromFormatStyle(formatStyle),
                    locale,
                )
            )
        }

        protected abstract fun formatNumber(
            value: Number,
            precision: Int,
            tailingFractionZeros: Boolean,
            locale: Locale,
        ): CharSequence
    }

    object NumberFormatter : BaseNumberFormatter("number") {
        override fun formatNumber(
            value: Number,
            precision: Int,
            tailingFractionZeros: Boolean,
            locale: Locale,
        ): CharSequence {
            return LocalizedStringNumber.formatNumber(
                value,
                precision,
                tailingFractionZeros,
                locale
            )
        }
    }

    object LengthFormatter : BaseNumberFormatter("length") {
        override fun formatNumber(
            value: Number,
            precision: Int,
            tailingFractionZeros: Boolean,
            locale: Locale,
        ): CharSequence {
            return LocalizedStringNumber.getFormattedLength(
                value.toDouble(),
                value.toDouble(),
                precision,
                tailingFractionZeros,
            ).toString(locale)
        }
    }

    object AreaFormatter : BaseNumberFormatter("area") {
        override fun formatNumber(
            value: Number,
            precision: Int,
            tailingFractionZeros: Boolean,
            locale: Locale,
        ): CharSequence {
            return LocalizedStringNumber.getFormattedArea(
                value.toDouble(),
                value.toDouble(),
                precision,
                tailingFractionZeros,
            ).toString(locale)
        }
    }

    object WeightFormatter : BaseNumberFormatter("weight") {
        override fun formatNumber(
            value: Number,
            precision: Int,
            tailingFractionZeros: Boolean,
            locale: Locale,
        ): CharSequence {
            return LocalizedStringNumber.getFormattedWeight(
                value.toDouble(),
                value.toDouble(),
                precision,
                tailingFractionZeros,
            ).toString(locale)
        }
    }

    // no count of digit supported
    object TimespanFormatter : BaseNumberFormatter("timespan") {
        override fun formatNumber(
            value: Number,
            precision: Int,
            tailingFractionZeros: Boolean,
            locale: Locale,
        ): CharSequence {
            return LocalizedStringNumber.getFormattedTimeSpan(value.toDouble())
                .toString(locale)
        }
    }

    /** Parses the `formatStyle` and return the number of digits after the fraction sign. */
    private fun parsePrecisionFromFormatStyle(formatStyle: CharSequence?): Int {
        if (formatStyle == null || !formatStyle.startsWith("%"))
            return Int.MAX_VALUE
        var idxStart = formatStyle.indexOf(".")
        if (idxStart < 0 || idxStart + 1 >= formatStyle.length)
            return Int.MAX_VALUE
        idxStart++
        var idxEnd = idxStart
        if (formatStyle[idxEnd] == '-')
            idxEnd++
        while (formatStyle.length > idxEnd && formatStyle[idxEnd].isDigit())
            idxEnd++
        return formatStyle.subSequence(idxStart, idxEnd).toString().toIntOrNull() ?: Int.MAX_VALUE
    }

    /** Parses the `formatStyle` and return the number of digits after the fraction sign. */
    private fun parseTailingFractionZerosFromFormatStyle(formatStyle: CharSequence?): Boolean {
        if (formatStyle == null || !formatStyle.startsWith("%"))
            return false
        return formatStyle.contains(".0")

    }
}