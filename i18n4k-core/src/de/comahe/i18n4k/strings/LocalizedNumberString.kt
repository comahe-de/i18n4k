@file:Suppress("unused")

package de.comahe.i18n4k.strings

import de.comahe.i18n4k.*
import kotlin.jvm.JvmOverloads
import kotlin.math.*

/** Provies locale specific formatting of numbers. e.g. "12.345,67" */
@Suppress("MemberVisibilityCanBePrivate")
data class LocalizedStringNumber @JvmOverloads constructor(
    /** the number to be formatted */
    var number: Number,
    /**
     * The position to which the rounding is to be made.
     *
     * Positive for after the decimal point, negative for before the decimal point, 0 for without decimal places.
     * [Int.MAX_VALUE] for adaptive precision (show all float digits that are not 0)
     */
    val precision: Int = Int.MAX_VALUE,

    /** Add zeros after the fraction sign until digits count of [precision] is reached.
     *  Only if [precision] is greater than 0 and not [Int.MAX_VALUE] */
    val tailingFractionZeros: Boolean = false

) : AbstractLocalizedString() {

    override fun toString() =
        toString(null)

    override fun toString(locale: Locale?) =
        formatNumber(number, precision, tailingFractionZeros, locale ?: i18n4k.locale).toString()


    companion object {

        /** the default number formatting (English style) */
        private val defaultNumberFormattingInfo = NumberFormattingInfo(
            fractionSign = '.',
            groupingSign = ',',
            groupIntegerSize = 3,
            groupFractionSize = 0
        )

        /** [NumberFormattingInfo] pre locale ID */
        val numberFormattingInfoPerLocaleId = mutableMapOf(
            "de" to NumberFormattingInfo(
                fractionSign = ',',
                groupingSign = '.',
                groupIntegerSize = 3,
                groupFractionSize = 0
            )
        )

        /**
         * Formats a number using the given parameters
         *
         * @param
         *      Number to be formatted
         * @param precision
         *      see [LocalizedStringNumber.precision]
         * @param tailingFractionZeros
         *      see [LocalizedStringNumber.tailingFractionZeros]
         * @param locale
         *      Locale to be used.
         */
        fun formatNumber(
            number: Number,
            precision: Int,
            tailingFractionZeros: Boolean,
            locale: Locale
        ): CharSequence {
            val b: StringBuilder = when (precision) {
                Int.MAX_VALUE -> doubleToString(number.toDouble())
                else -> roundToString(number.toDouble(), precision)
            }
            if (!tailingFractionZeros)
                removeTailingZeros(b)

            val f = getNumberFormattingInfo(locale)

            var fractionSignIndex = b.indexOf(".")
            if (fractionSignIndex < 0 && (f.groupIntegerSize <= 0 || b.length < f.groupIntegerSize))
                return b.toString()

            val startsWithPlusOrMinus = b[0] == '+' || b[0] == '-'

            if (fractionSignIndex >= 0) {
                b[fractionSignIndex] = f.fractionSign
                if (f.groupFractionSize > 0) {
                    var idx = fractionSignIndex + 1 + f.groupFractionSize
                    while (idx < b.length) {
                        b.insert(idx, f.groupingSign)
                        idx += 1 + f.groupFractionSize
                    }
                }
            } else
                fractionSignIndex = b.length

            if (f.groupIntegerSize > 0) {
                var idx = fractionSignIndex - f.groupIntegerSize
                while (if (startsWithPlusOrMinus) idx > 1 else idx > 0) {
                    b.insert(idx, f.groupingSign)
                    idx -= f.groupIntegerSize
                }
            }
            return b.toString()

        }

        /** Luck up the [NumberFormattingInfo] to be used for the given locale */
        fun getNumberFormattingInfo(locale: Locale?): NumberFormattingInfo {
            val f = if (locale == null) null else numberFormattingInfoPerLocaleId[locale.language]
            return f ?: defaultNumberFormattingInfo
        }

        /**
         * Rounds to the given precession
         *
         * @param number
         *      Number to be formatted
         * @param precision The position to which the rounding is to be made. Positive for after the decimal point,
         *        negative for before the decimal point, 0 for without decimal places .
         * @return the rounded number as string
         */
        private fun roundToString(number: Double, precision: Int): StringBuilder {

            val negativ = number < 0.0
            var modifiedNumber = abs(number)
            val f = 10.0.pow(-precision.toDouble())
            if (precision != 0)
                modifiedNumber /= f
            val text: StringBuilder

            if (precision < 0) {
                val roundedValue = round(modifiedNumber) * f
                if (roundedValue == 0.0)
                    return StringBuilder("0")
                text = StringBuilder(roundedValue.toString())
                removeTailingZeros(text)
            } else if (modifiedNumber > Long.MAX_VALUE) {
                text = StringBuilder(number.toString())
            } else {
                val longValue = modifiedNumber.roundToLong()
                if (longValue == 0L) {
                    text = StringBuilder("0")
                    if (precision > 0) {
                        text.append(".")
                        for (i in 1..precision)
                            text.append("0")
                    }
                    return text
                }

                text = StringBuilder(longValue.toString())
                if (precision > 0) {
                    var signIndex = text.length - precision
                    while (signIndex < 0) {
                        text.insert(0, "0")
                        signIndex++
                    }
                    text.insert(signIndex, if (signIndex == 0) "0." else ".")
                }
            }
            if (negativ)
                text.insert(0, "-")
            normalizeNumber(text)
            return text
        }

        /** Transforms double to string. Avoids e.g. 12.345678900000001 for 12.3456789 by rounding the last two digits */
        private fun doubleToString(number: Double): StringBuilder {
            var text = StringBuilder(number.toString())
            normalizeNumber(text)
            val fractionSignIndex = text.indexOf(".")

            if (text.contains(char = 'e', ignoreCase = true)) {
                val exponentSignIndex = max(text.indexOf("e"), text.indexOf("E"))
                var exponent = text.substring(exponentSignIndex + 1).toInt()
                if (exponent < 0 && fractionSignIndex >= 0 && fractionSignIndex < exponentSignIndex)
                    exponent -= exponentSignIndex - fractionSignIndex - 1
                if (abs(exponent) >= 16)
                    return text
                if (exponent < 0)
                    text = roundToString(number, -exponent)
                else
                    text = roundToString(number, 14 - exponent)
            } else {
                if (fractionSignIndex < 0)
                    return text
                var numberLength = text.length
                if (number < 0)
                    numberLength--
                if (abs(number) < 1)
                    numberLength--

                if (numberLength > 16)
                    text = roundToString(number, max(0, text.length - fractionSignIndex - 3))
            }
            removeTailingZeros(text)
            return text
        }

        /** removes the tailing zeros after the fraction sign. */
        private fun removeTailingZeros(text: StringBuilder) {
            val fractionSignIndex = text.indexOf(".")
            if (fractionSignIndex < 0)
                return

            //remove tailing zeros
            for (i in text.length - 1 downTo fractionSignIndex) {
                val digit = text[i]
                if (digit == '.') {// *.0
                    text.setLength(i)
                    break
                }
                if (digit != '0') {// *.*0
                    text.setLength(i + 1)
                    break
                }
            }
        }

        /** lower case and not "+"-signs */
        private fun normalizeNumber(text: StringBuilder) {
            for (i in text.indices) {
                if (!text[i].isDigit())
                    text[i] = text[i].lowercaseChar()
            }
            while (true) {
                val plusSign = text.indexOf("+")
                if (plusSign < 0)
                    break
                text.deleteAt(plusSign)
            }
        }

        /**
         * Creates a [LocalizedString] that contains a text with a length
         *
         * @param meters the length in m
         */
        fun getFormattedLength(meters: Double): LocalizedString {
            return getFormattedLength(meters, meters, Int.MAX_VALUE, true)
        }

        /**
         * Creates a [LocalizedString] that contains a text with a length
         *
         * @param meters
         *      the length in m
         * @param unitValue
         *      factor that determines the technical postfix (1=m, 1000=km, 0.001=mm, ...).
         *      For automatic selection, pass the same value as for `meters`.
         * @param precision
         *     see  [LocalizedStringNumber.precision]
         * @param tailingFractionZeros
         *     see [LocalizedStringNumber.tailingFractionZeros]
         */
        fun getFormattedLength(
            meters: Double, unitValue: Double,
            precision: Int,
            tailingFractionZeros: Boolean,
        ): LocalizedString {
            @Suppress("NAME_SHADOWING") var meters = meters
            @Suppress("NAME_SHADOWING") var unitValue = unitValue

            getSpecialDoubleValuesString(meters)?.let { return SimpleLocalizedString(it) }

            unitValue = abs(unitValue)
            var unit = "m"
            if (unitValue < 1) {
                meters *= 1000.0
                unit = "mm"
            } else if (unitValue >= 1000) {
                meters *= 0.001
                unit = "km"
            }
            return ParameterisedLocalizedString(
                "{0} {1}",
                listOf(LocalizedStringNumber(meters, precision, tailingFractionZeros), unit)
            )
        }

        /**
         * Creates a [LocalizedString] that contains a text with a weight
         *
         * @param grams
         *      the weight in g
         */
        fun getFormattedWeight(grams: Double): LocalizedString {
            return getFormattedWeight(grams, grams, Int.MAX_VALUE, true)
        }

        /**
         * Creates a [LocalizedString] that contains a text with a weight
         *
         * @param grams
         *      the weight in g
         * @param unitValue
         *      factor that determines the technical postfix (1=g, 1000=kg, 0.001=mg, ...).
         *      For automatic selection, pass the same value as for `grams`.
         * @param precision
         *     see  [LocalizedStringNumber.precision]
         * @param tailingFractionZeros
         *     see [LocalizedStringNumber.tailingFractionZeros]
         */
        fun getFormattedWeight(
            grams: Double, unitValue: Double,
            precision: Int,
            tailingFractionZeros: Boolean,
        ): LocalizedString {
            @Suppress("NAME_SHADOWING") var grams = grams
            @Suppress("NAME_SHADOWING") var unitValue = unitValue

            getSpecialDoubleValuesString(grams)?.let { return SimpleLocalizedString(it) }

            unitValue = abs(unitValue)
            var unit = "g"
            when {
                unitValue < 1 -> {
                    grams *= 1_000.0
                    unit = "mg"
                }
                unitValue >= 1000_000 -> {
                    grams *= 0.000_001
                    unit = "t"
                }
                unitValue >= 1000 -> {
                    grams *= 0.001
                    unit = "kg"
                }

            }
            return ParameterisedLocalizedString(
                "{0} {1}",
                listOf(LocalizedStringNumber(grams, precision, tailingFractionZeros), unit)
            )
        }

        /**
         * Creates a [LocalizedString] that contains a text with an area
         *
         * @param squareMeters
         *      the area in m²
         */
        fun getFormattedArea(squareMeters: Double): LocalizedString {
            return getFormattedArea(squareMeters, squareMeters, Int.MAX_VALUE, true)
        }

        /**
         *  Creates a [LocalizedString] that contains a text with an area
         *
         * @param squareMeters
         *      the area in m²
         * @param unitValue
         *      factor that determines the technical postfix (1=m², 1000000=km², 0.000001=mm², ...).
         *      For automatic selection, pass the same value as for `squareMeters`.
         * @param precision
         *     see  [LocalizedStringNumber.precision]
         * @param tailingFractionZeros
         *     see [LocalizedStringNumber.tailingFractionZeros]
         */
        fun getFormattedArea(
            squareMeters: Double, unitValue: Double,
            precision: Int,
            tailingFractionZeros: Boolean,
        ): LocalizedString {
            @Suppress("NAME_SHADOWING") var squareMeters = squareMeters
            @Suppress("NAME_SHADOWING") var unitValue = unitValue

            getSpecialDoubleValuesString(squareMeters)?.let { return SimpleLocalizedString(it) }

            unitValue = abs(unitValue)
            var unit = "m²"
            if (unitValue < .001) {
                squareMeters *= 1000000.0
                unit = "mm²"
            }
            if (unitValue >= 100000) {
                squareMeters /= 1000000.0
                unit = "km²"
            }
            return ParameterisedLocalizedString(
                "{0} {1}",
                listOf(LocalizedStringNumber(squareMeters, precision, tailingFractionZeros), unit)
            )
        }


        /**
         * Creates a [LocalizedString] that contains a text with a time span
         *
         * @param durationInSeconds
         */
        fun getFormattedTimeSpan(durationInSeconds: Double): LocalizedString {
            @Suppress("NAME_SHADOWING") var durationInSeconds = durationInSeconds

            getSpecialDoubleValuesString(durationInSeconds)?.let { return SimpleLocalizedString(it) }

            var sign = ""
            if (durationInSeconds < 0.0) {
                sign = "-"
                durationInSeconds = -durationInSeconds
            }
            if (durationInSeconds > Int.MAX_VALUE) return SimpleLocalizedString('!')
            if (durationInSeconds < 1.0) {
                return SimpleLocalizedString("$sign$durationInSeconds s")
            }
            if (durationInSeconds < 60.0) {
                val seconds = LocalizedStringNumber(durationInSeconds, 1)
                return ParameterisedLocalizedString("$sign{0} s", listOf(seconds))
            }
            val secondsTotal = round(durationInSeconds).toInt()
            val seconds = secondsTotal % 60
            val minutes = secondsTotal / 60 % 60
            val hours = secondsTotal / 60 / 60 % 24
            val days = secondsTotal / 60 / 60 / 24
            val sb = StringBuilder()
            sb.append(sign)
            if (days > 0) sb.append(days).append("d ")
            if (hours > 0) {
                sb.append(hours)
                if (minutes > 0 || seconds > 0) {
                    sb.append(":")
                    if (minutes < 10)
                        sb.append("0")
                    sb.append(minutes)
                }
                if (seconds > 0) {
                    sb.append(":")
                    if (seconds < 10)
                        sb.append("0")
                    sb.append(seconds)
                }
                sb.append(" h")
            } else if (minutes > 0) {
                sb.append(minutes)
                if (seconds > 0) {
                    sb.append(":")
                    if (seconds < 10)
                        sb.append("0")
                    sb.append(seconds)
                }
                sb.append(" min")
            } else if (seconds > 0) {
                sb.append(seconds).append(" s")
            }
            return SimpleLocalizedString(sb.toString())
        }

        private fun getSpecialDoubleValuesString(v: Double): String? {
            if (v.isInfinite()) {
                return if (v > 0) "\u221E" else "-\u221E"
            }
            return if (v.isNaN()) "NaN" else null
        }
    }

    /** Hold information to format a number */
    class NumberFormattingInfo(
        /** Sign to separate integer part from the fraction part of the number */
        val fractionSign: Char,
        /** Sign to separate large sequences of digits, e.g 123.456.790,12 */
        val groupingSign: Char,
        /** Size of groups of the integer part. 0 for no grouping  */
        val groupIntegerSize: Int,
        /** Size of groups of the fraction part. 0 for no grouping  */
        val groupFractionSize: Int
    )
}

/**
 * Replacement for Char.isDigit() (not available on all platforms)
 *
 * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/is-digit.html
 */
internal fun Char.isDigit(): Boolean {
    return when (this) {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true
        else -> false
    }
}