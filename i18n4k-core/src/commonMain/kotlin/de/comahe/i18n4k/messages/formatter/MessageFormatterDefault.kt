package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault.valueFormatters
import de.comahe.i18n4k.strings.LocalizedString
import kotlinx.atomicfu.TraceBase.None.append

/**
 * Default formatter for messages of `i18n4k`
 *
 * Format must be similar to the Java MessageFormat:
 *
 * MessageFormat uses patterns of the following form:
 * ```
 * MessageFormatPattern:
 *      String
 *      MessageFormatPattern FormatElement String
 *
 * FormatElement:
 *      { ArgumentIndex }
 *      { ArgumentIndex , FormatType }
 *      { ArgumentIndex , FormatType , FormatStyle }
 *
 * ArgumentIndex: argument with the given index in the parameter list. Zero-base (0 is the index of the first argument)
 *
 * FormatType: type to format, like number,date, etc. Defined be registered formatter
 *
 * FormatStyle: Specific style to the FormatType
 *
 * ```
 *
 * Within a String, a pair of single quotes can be used to quote any
 * arbitrary characters except single quotes. For example, pattern string
 * "'{0}'" represents string "{0}", not a FormatElement. A single quote
 * itself must be represented by doubled single quotes '' throughout
 * a String. For example, pattern string "'{''}'" is interpreted as a
 * sequence of '{ (start of quoting and a left curly brace), '' (a single
 * quote), and }' (a right curly brace and end of quoting), not '{' and '}'
 * (quoted left and right curly braces): representing string "{'}", not
 * "{}".
 *
 * Any unmatched quote is treated as closed at the end of the given
 * pattern. For example, pattern string "'{0}" is treated as pattern
 * "'{0}'".
 *
 * Any curly braces within an unquoted pattern must be balanced. For
 * example, "ab {0} de" and "ab '}' de" are valid patterns, but "ab {0'}'
 * de", "ab } de" and "''{''" are not.
 *
 * The `FormatType` is evaluated by [MessageValueFormatter]. The
 * [MessageValueFormatter] defines the possible values of the
 * `FormatStyle`.
 *
 * The following [MessageValueFormatter] are added by default
 * * [MessageNumberFormatter]
 */
object MessageFormatterDefault : MessageFormatter {

    private val valueFormatters = mapOf<CharSequence, MessageValueFormatter>(
        MessageNumberFormatter.FORMAT_STYLE_AREA to MessageNumberFormatter,
        MessageNumberFormatter.FORMAT_STYLE_LENGTH to MessageNumberFormatter,
        MessageNumberFormatter.FORMAT_STYLE_NUMBER to MessageNumberFormatter,
        MessageNumberFormatter.FORMAT_STYLE_TIMESPAN to MessageNumberFormatter,
        MessageNumberFormatter.FORMAT_STYLE_WEIGHT to MessageNumberFormatter
    )

    override fun format(message: String, parameters: List<Any>, locale: Locale): String {
        val buffer = StringBuilder(message.length * 2)
        var argumentPartStartIndex = 0
        var argumentIndex: CharSequence? = null
        var argumentFormatType: CharSequence? = null
        var argumentFormatStyle: CharSequence? = null
        var inQuotes = false
        var inBraces = false
        var oneQuote = false
        var index = 0
        for (c in message) {
            when {
                oneQuote -> {
                    if (c == '\'')
                        buffer.append('\'')
                    else {
                        buffer.append(c)
                        inQuotes = !inQuotes
                    }
                    oneQuote = false
                }
                inQuotes -> {
                    if (c == '\'')
                        oneQuote = true
                    else {
                        buffer.append(c)
                    }
                }

                inBraces -> {
                    if (c == ',' || c == '}') {
                        val argumentPart = message.subSequence(argumentPartStartIndex, index).trim()
                        argumentPartStartIndex = index + 1

                        when {
                            argumentIndex == null -> argumentIndex = argumentPart
                            argumentFormatType == null -> argumentFormatType = argumentPart
                            argumentFormatStyle == null -> argumentFormatStyle = argumentPart
                        }
                    }
                    if (c == '}') {// end of argument
                        var argObject: Any? = null
                        val argIndex = argumentIndex.toString().toIntOrNull()
                        if (argIndex !== null && parameters.size > argIndex)
                            argObject = parameters[argIndex]
                        if (argObject == null)
                            buffer.append('{').append(argumentIndex).append('}')
                        else
                            buffer.append(formatParameter(argObject, argumentFormatType, argumentFormatStyle, locale))
                        inBraces = false
                        argumentIndex = null
                        argumentFormatType = null
                        argumentFormatStyle = null
                    }
                }
                c == '\'' -> {
                    oneQuote = true
                }

                c == '{' -> {
                    inBraces = true
                    argumentPartStartIndex = index + 1
                }
                else -> {
                    buffer.append(c)
                }
            }
            index++
        }
        return buffer.toString()
    }

    private fun formatParameter(
        p: Any,
        formatType: CharSequence?,
        formatStyle: CharSequence?,
        locale: Locale
    ): CharSequence {
        if (formatType != null) {
            valueFormatters[formatType]
                ?.format(p, formatType, formatStyle, locale)
                ?.let { return it }
        }
        if (p is LocalizedString)
            return p.toString(locale)
        return p.toString()
    }
}