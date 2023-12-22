// Copyright © 2020 xyzsd
//
// See the COPYRIGHT file at the top-level directory of this
// distribution.
//
// Licensed under the Apache License, Version 2.0 <LICENSE-APACHE or
// http://www.apache.org/licenses/LICENSE-2.0> or the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT>, at your
// option. This file may not be copied, modified, or distributed
// except according to those terms.

// converted to Kotlin MP by Marcel Heckel
package de.comahe.i18n4k.cldr.plurals

import kotlin.jvm.JvmOverloads
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.pow

/**
 * Wrapper for numbers
 *
 * PluralOperands contain information about the structure of a number, which can change plural
 * conventions depending upon the language/locale.
 *
 * A BigDecimal or String can be used instead of numeric types to better establish precision (for
 * example, trailing zeros), for which the handling of plural forms can be locale-dependent.
 *
 * If a number is expressed in compact form, the exponent is suppressed. For example, "2.3 Million"
 * the exponent is 6. This can change pluralization for certain languages. Use fromCompact() methods
 * to explicitly suppress an exponent.
 */
class PluralOperand private constructor(
    // see: http://unicode.org/reports/tr35/tr35-numbers.html#Language_Plural_Rules
    /** absolute value of input (integer and decimals) */
    val n: Double,
    /* integer digits of n (also always >= 0) */
    val i: Long,
    /* count of visible fraction digits WITH trailing zeros */
    val v: Int,
    /* count of visible fraction digits WITHOUT trailing zeros */
    val w: Int,
    /* visible fraction digits WITH trailing zeros */
    val f: Int,
    /* visible fraction digits WITHOUT trailing zeros */
    val t: Int,
    /* suppressed exponent [added in CLDR 38], synonym for 'c' (!) */
    val e: Int
) {
    /** For debugging, not display. */
    override fun toString(): String {
        return "PluralOperand{" +
            "n=" + n +
            ", i=" + i +
            ", v=" + v +
            ", w=" + w +
            ", f=" + f +
            ", t=" + t +
            ", e=" + e +
            '}'
    }

    companion object {
        /**
         * Create a PluralOperand from a String.
         *
         * Using a String or BigDecimal instead of a `float` or `double` allows for the
         * determination of trailing zeros and visible fraction digits, which can have an impact on
         * number format selection depending upon the locale.
         *
         * @param s a Number, as a String. Null-safe.
         * @return PluralOperand, or empty Optional if the String cannot be parsed into a BigDecimal
         */
        fun from(s: String?, suppressedExponent: Int = 0): PluralOperand? {
            return if (s.isNullOrEmpty()) {
                null
            } else try {
                if (s.contains("."))
                    from(s.toDouble(), s, suppressedExponent)
                else
                    from(s.toLong(), suppressedExponent)
            } catch (e: NumberFormatException) {
                null
            }
        }

        /**
         * Create a PluralOperand from a given `Number` type.
         *
         * This selects the most specific type:
         * * BigInteger, BigDecimal: handled using BigDecimal type
         * * Double, Float: handled using double type
         * * all others (long, int, short, byte): handled using long type
         *
         * Null values will result in a NullPointerException
         *
         * @param input Number
         * @return PluralOperand
         */
        fun from(input: Number, suppressedExponent: Int = 0): PluralOperand {
            return if (input is Double || input is Float)
                from(input.toDouble(), suppressedExponent)
            else
                from(input.toLong(), suppressedExponent)
        }

        /**
         * Explicitly set suppressedExponent for Compact format numbers. The exponent is
         * explicitly denoted; e.g., fromCompact(1L, 6) is equivalent to "1 Million" However,
         * fromCompact(1000000L, 6) will result in "1000000 million"
         *
         * @param input input as a long
         * @param suppressedExponent (must be between 0 and 21)
         * @return PluralOperand
         */
        fun from(input: Int, suppressedExponent: Int = 0): PluralOperand {
            return from(input.toLong(), suppressedExponent)
        }

        /**
         * Explicitly set suppressedExponent for Compact format numbers. The exponent is
         * explicitly denoted; e.g., fromCompact(1L, 6) is equivalent to "1 Million" However,
         * fromCompact(1000000L, 6) will result in "1000000 million"
         *
         * @param input input as a long
         * @param suppressedExponent (must be between 0 and 21)
         * @return PluralOperand
         */
        fun from(input: Long, suppressedExponent: Int = 0): PluralOperand {
            checkSE(suppressedExponent)

            var expanded = input

            if (suppressedExponent > 0) {
                // too large from long?
                if (expanded * 10.0.pow(suppressedExponent) > Long.MAX_VALUE)
                    return from(input.toDouble(), input.toString(), suppressedExponent)

                repeat(suppressedExponent) { expanded *= 10 }
            }

            expanded = if (expanded != Long.MIN_VALUE)
                expanded.absoluteValue
            else
                Long.MAX_VALUE

            return PluralOperand(
                expanded.toDouble(),
                expanded,
                0, 0, 0, 0,
                suppressedExponent
            )
        }

        /**
         * Explicitly set suppressedExponent for Compact format numbers.
         *
         * The exponent must be explicitly denoted; e.g., fromCompact(1L, 6) is equivalent to "1
         * Million" However, fromCompact(1000000L, 6) will result in "1000000 million"
         *
         * Note that for `fromCompact(1.2, 6)` the operands are (n = 1.2, i = 1200000, c = 6)
         *
         * It is not possible to determine precision (trailing zeros) from double input.
         *
         * @param input input as a double
         * @param suppressedExponent (must be between 0 and 21)
         * @return PluralOperand
         */
        @JvmOverloads
        fun from(input: Double, suppressedExponent: Int = 0): PluralOperand {
            return from(input, input.toString(), suppressedExponent)
        }

        private fun from(
            input: Double,
            inputString: String,
            suppressedExponent: Int
        ): PluralOperand {
            checkSE(suppressedExponent)

            // TODO: this may not always handle exponential correctly....
            //        since f, t must fit into an int: use 9 decimal places max
            //        so we should set the MathContext appropriately and adjust precision
            //        as needed

            // NOTE:
            //       Converting to a Double/Long may lose precision.
            //       But, these values are *not* used for display, just plural selection.
            //       But we will try to calculate fractional digits as appropriate.
            //

            // the original implementation ues BigInteger, but this is not available in KMP!
            // So try to do it with strings...

            val absIn = (
                if (suppressedExponent == 0)
                    input
                else
                    input * 10.0.pow(suppressedExponent)
                ).absoluteValue


            // these will not be accurate for (very) large numbers. In most cases that will be OK.
            val n = min(absIn, Double.MAX_VALUE) // disallow +infinity
            val i = absIn.toLong()

            // input string without minus
            val inputStringAbs =
                if (inputString.startsWith("-")) inputString.substring(1)
                else inputString
            // digits to the right of the decimal point
            val fractional: String =
                if (inputStringAbs.contains("e") || inputStringAbs.contains("E")) {
                    val numberString = absIn.toString()
                    if (numberString.contains("e") || numberString.contains("E")) {
                        // cannot handle exponents in number string...
                        ""
                    } else {
                        val indexPoint = numberString.indexOf(".")
                        if (indexPoint < 0)
                            ""
                        else
                            numberString.substring(indexPoint + 1)
                    }
                } else {
                    val indexPoint = inputStringAbs.indexOf(".")
                    if (indexPoint < 0)
                        ""
                    else if (inputStringAbs.length - indexPoint > suppressedExponent)
                        inputStringAbs.substring(indexPoint + suppressedExponent + 1)
                    else
                        run {
                            val zeros = StringBuilder()
                            repeat(suppressedExponent - indexPoint) { zeros.append("0") }
                            zeros.toString()
                        } +
                            inputStringAbs.substring(0, indexPoint) +
                            inputStringAbs.substring(indexPoint + 1)

                }


            return if (fractional.isEmpty()) {
                PluralOperand(n, i, 0, 0, 0, 0, suppressedExponent)
            } else {
                // visible fraction digits WITHOUT trailing zeros
                val fractionalStripped = fractional.substring(
                    0,
                    fractional.lastIndexOfAny(
                        charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')
                    ) + 1
                )
                // visible fraction digit count: WITH trailing zeros
                val v = fractional.length

                // visible fraction digit count: WITHOUT trailing zeros
                val w = fractionalStripped.length

                // visible fraction digits WITH trailing zeros
                val f = fractional.toInt()

                // visible fraction digits WITHOUT trailing zeros
                val t = if (fractionalStripped.isEmpty()) 0 else fractionalStripped.toInt()
                PluralOperand(n, i, v, w, f, t, suppressedExponent)
            }
        }

        /*
    notes: visible fraction digits (with/without) trailing zeroes must fit into an int
    therefore: 2 147 483 647
        we can therefore restrict to 9 decimal places


     */
        // suppressed exponent check. Acceptable values: [0,21]
        private fun checkSE(i: Int) {
            require(!(i < 0 || i > 21)) { "Exponent out of range [0,21]: $i" }
        }
    }
}
