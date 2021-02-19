package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.strings.LocalizedStringNumber
import de.comahe.i18n4k.strings.isDigit
import de.comahe.i18n4k.strings.LocalizedString

/** A [MessageValueFormatter] for numbers ([Number])
 *
 * Supported values for `FormatType` (see FORMAT_STYLE_* constants)
 * * number
 * * length
 * * area
 * * weight
 * * timespan
 *
 *
 *  Supported values for `FormatStyle`
 *  * "%.0P":
 *     * P: precision: If given, the position to which the rounding is to be made.
 *       Positive for after the decimal point, negative for before the decimal point, 0 for without decimal places.
 *     * 0: Tailing zeros:  If given and `P` is given an positive,
 *      `P` is the exact number digits after the fraction sign. (zeros are appended if necessary)
 *
 *
 *
 *
 * Usage examples for number = 12345.6789:
 * * `{0,number}` - Format parameter 0 as number, e.g. "12,345.6789"
 * * `{0,number, %.2}`  - Format parameter 0 as number with max. 2 fraction digits, e.g. "12,345.68"
 * * `{0,number, %.06}`  - Format parameter 0 as number with exact. 6 fraction digits, e.g. "12,345.678900"
 * * `{0,length}`  - Format parameter 0 as length (in meters), e.g. "12.3456789 km"
 * * `{0,length, %.2}`  - Format parameter 0 as length (in meters) with 2 fraction digits, e.g. "12.35 km"
 * * `{0,area}`  - Format parameter 0 as area (in m²), e.g. "12,345.6789 m²"
 * * `{0,area, %.2}`  - Format parameter 0 as area (in m²) with 2 fraction digits, e.g. "12,345.68 m²"
 * * `{0,weight}`  - Format parameter 0 as weight (in grams), e.g. "12.3456789 kg"
 * * `{0,weight, %.2}`  - Format parameter 0 as weight (in grams) with 2 fraction digits, e.g. "12.35 kg"
 * * `{0,timespan}`  - Format parameter 0 as timespan (in seconds), e.g. "3:25:45 h"
 *
 *
 * */
object MessageNumberFormatter : MessageValueFormatter {
    //////////////////////
    const val FORMAT_STYLE_NUMBER = "number"
    const val FORMAT_STYLE_LENGTH = "length"
    const val FORMAT_STYLE_AREA = "area"
    const val FORMAT_STYLE_WEIGHT = "weight"

    // no count of digit supported
    const val FORMAT_STYLE_TIMESPAN = "timespan"

    override fun format(
        value: Any,
        formatType: CharSequence,
        formatStyle: CharSequence?,
        locale: Locale
    ): CharSequence {

        if (value !is Number) {
            if (value is LocalizedString)
                return value.toString(locale)
            return value.toString()
        }

        val precision = parsePrecisionFromFormatStyle(formatStyle)
        val tailingFractionZeros = parseTailingFractionZerosFromFormatStyle(formatStyle)

        return when (formatType) {
            FORMAT_STYLE_NUMBER -> LocalizedStringNumber.formatNumber(
                value,
                precision,
                tailingFractionZeros,
                locale
            )
            FORMAT_STYLE_LENGTH -> LocalizedStringNumber.getFormattedLength(
                value.toDouble(),
                value.toDouble(),
                precision,
                tailingFractionZeros,
            ).toString(locale)
            FORMAT_STYLE_AREA -> LocalizedStringNumber.getFormattedArea(
                value.toDouble(),
                value.toDouble(),
                precision,
                tailingFractionZeros,
            ).toString(locale)
            FORMAT_STYLE_WEIGHT -> LocalizedStringNumber.getFormattedWeight(
                value.toDouble(),
                value.toDouble(),
                precision,
                tailingFractionZeros,
            ).toString(locale)
            FORMAT_STYLE_TIMESPAN -> LocalizedStringNumber.getFormattedTimeSpan(value.toDouble())
                .toString(locale)
            else -> value.toString()
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