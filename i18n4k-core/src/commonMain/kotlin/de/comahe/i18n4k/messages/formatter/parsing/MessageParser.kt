package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.i18n4k
import kotlinx.collections.immutable.toImmutableList

/** Parser for the [de.comahe.i18n4k.messages.formatter.MessageFormatterDefault] format */
class MessageParser(
    private val message: CharSequence,
    private val ignoreMessageParseErrors: Boolean = i18n4k.ignoreMessageParseErrors
) {

    /** Parses the message */
    fun parseMessage(): MessagePart {
        return parseMessagePart(0, message.length)
    }

    /**
     * Parses the message in the given range.
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseMessagePart(startIndex: Int, endIndex: Int): MessagePart {
        var index = findNextOpenBrace(startIndex, endIndex)

        if (!ignoreMessageParseErrors && index == endIndex - 1)
            throw MessageParseException("Missing closing brace")
        if (index >= endIndex - 1)
            return MessagePartText(readText(startIndex, index))


        var lastIndex = startIndex
        val parts = mutableListOf<MessagePart>()

        while (true) {

            if (index > lastIndex)
                parts += MessagePartText(readText(lastIndex, index))

            lastIndex = index + 1
            if (lastIndex >= endIndex)
                break

            // in braces
            index = findCorrespondingCloseBrace(lastIndex, endIndex)
            if (!ignoreMessageParseErrors && index >= endIndex)
                throw MessageParseException("Missing closing brace")


            try {
                val part = parseParameterPattern(lastIndex, index)
                // skip empty parts
                if (part !is MessagePartText || part.text.isNotEmpty())
                    parts += part
            } catch (e: MessageParseException) {
                if (!ignoreMessageParseErrors)
                    throw e
            }

            lastIndex = index + 1
            if (lastIndex >= endIndex)
                break

            index = findNextOpenBrace(index, endIndex)
        }

        if (parts.isEmpty()) {
            if (ignoreMessageParseErrors)
                return MessagePartText("")
            throw MessageParseException("Missing closing brace")
        }
        if (parts.size == 1)
            return parts[0]
        return MessagePartList(parts.toImmutableList())
    }

    /**
     * Parses the message in the given range, but trims whitespaces at the beginning and end.
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseMessagePartTrimmed(startIndex: Int, endIndex: Int): MessagePart {
        var startIndexNew = startIndex
        var endIndexNew = endIndex
        while (message.length > startIndexNew && message[startIndexNew].isWhitespace())
            startIndexNew++
        while (startIndexNew < endIndexNew && message[endIndexNew - 1].isWhitespace())
            endIndexNew--

        return parseMessagePart(startIndexNew, endIndexNew)
    }

    /**
     * Parses the parameter pattern, e.g. "{0, type, style}"
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseParameterPattern(startIndex: Int, endIndex: Int): MessagePart {
        var lastIndex = startIndex

        // read parameter index
        var index = findNextCorrespondingComma(lastIndex, endIndex)
        // check for invalid braces
        if (index > findNextOpenBrace(lastIndex, endIndex)
            || index > findNextCloseBrace(lastIndex, endIndex)
        ) {
            if (ignoreMessageParseErrors)
                return MessagePartText("")
            throw MessageParseException("Found not escaped braces in parameter name.")
        }
        val parameterIndex = parseParameterName(lastIndex, index)
        if (index >= endIndex)
            return MessagePartParam(parameterIndex, null, null)

        // read type
        lastIndex = index + 1
        index = findNextCorrespondingComma(lastIndex, endIndex)
        // check for invalid braces
        if (index > findNextOpenBrace(lastIndex, endIndex)
            || index > findNextCloseBrace(lastIndex, endIndex)
        ) {
            if (ignoreMessageParseErrors)
                return MessagePartText("")
            throw MessageParseException("Found not escaped braces in parameter type.")
        }
        val parameterType = parseTypeName(lastIndex, index)
        if (index >= endIndex)
            return MessagePartParam(parameterIndex, parameterType, null)

        // read style
        lastIndex = index + 1
        index = findNextCorrespondingComma(lastIndex, endIndex)
        if (index < endIndex)
            throw MessageParseException(
                "Parameter pattern has more the two commas! Pattern: ${
                    message.subSequence(startIndex, endIndex)
                } - Message: $message"
            )
        return MessagePartParam(parameterIndex, parameterType, parseStyle(lastIndex, index))
    }

    /**
     * Parses the name of the parameter
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseParameterName(startIndex: Int, endIndex: Int): CharSequence {
        val name = readText(startIndex, endIndex).trim()
        if (name.isEmpty())
            throw MessageParseException("Parameter name must not be empty or blank!")
        return name
    }

    /**
     * Parses the name of the type
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseTypeName(startIndex: Int, endIndex: Int): CharSequence {
        val name = readText(startIndex, endIndex).trim()
        if (name.isEmpty())
            throw MessageParseException("Type name must not be empty or blank!")
        return name
    }

    /**
     * Parses the list of styles of the parameter.
     *
     * Styles are separated by whitespaces. Single quotes and braces can be used union parts with
     * whitespaces.
     *
     * Styles are interpreted as simple text ([StylePartArgument]) or if they are enclosed by brace
     * as a nested complex message (any [StylePartMessage] instance)
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseStyle(startIndex: Int, endIndex: Int): StylePart {
        @Suppress("NAME_SHADOWING")
        val startIndex = trimStart(startIndex, endIndex)

        @Suppress("NAME_SHADOWING")
        val endIndex = trimEnd(startIndex, endIndex)

        var indexOpenBrace = findNextOpenBrace(startIndex, endIndex)
        var indexWhiteSpace = findNextWhitespace(startIndex, endIndex)
        if (indexOpenBrace >= endIndex && indexWhiteSpace >= endIndex)
            return StylePartArgument(readText(startIndex, endIndex))

        val parts = mutableListOf<StylePart>()

        var lastIndex = startIndex

        while (indexOpenBrace < endIndex || indexWhiteSpace < endIndex) {
            if (indexWhiteSpace < indexOpenBrace) {
                if (lastIndex < indexWhiteSpace)
                    parts += StylePartArgument(readText(lastIndex, indexWhiteSpace))

                lastIndex = findNextNonWhitespace(indexWhiteSpace, endIndex)
                // is there a quote, which findNext would ignore?
                val quoteIndex = message.indexOf("'", indexWhiteSpace)
                @Suppress("ConvertTwoComparisonsToRangeCheck")
                if (quoteIndex >= 0 && quoteIndex < lastIndex)
                    lastIndex = quoteIndex

            } else {
                //argument before '{' without whitespace?
                val argumentText = readText(lastIndex, indexOpenBrace).trim()
                if (argumentText.isNotEmpty())
                    parts += StylePartArgument(argumentText)
                // read the message part
                lastIndex = findCorrespondingCloseBrace(indexOpenBrace + 1, endIndex)
                parts.add(StylePartMessage(parseMessagePart(indexOpenBrace + 1, lastIndex)))
                if (lastIndex < endIndex)
                    lastIndex++
            }

            indexWhiteSpace = findNextWhitespace(lastIndex, endIndex)
            indexOpenBrace = findNextOpenBrace(lastIndex, endIndex)
        }
        if (lastIndex < endIndex)
            parts += StylePartArgument(readText(lastIndex, endIndex))
        if (parts.size == 0)
            return StylePartArgument("")
        if (parts.size == 1)
            return parts[0]
        return StylePartList(parts.toImmutableList())

    }

    /** finds the next "{" not quoted */
    private fun findNextOpenBrace(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, '{')
    }

    /** finds the next "}" not quoted */
    private fun findNextCloseBrace(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, '}')

    }

    /** finds the next "/" not quoted */
    private fun findSlash(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, '/')

    }

    /** finds the next whitespace not quoted */
    private fun findNextWhitespace(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex) { it.isWhitespace() }
    }

    /** finds the next non-whitespace not quoted */
    private fun findNextNonWhitespace(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex) { !it.isWhitespace() }
    }

    /** finds the next "}" not quoted and not part of another "{" */
    private fun findCorrespondingCloseBrace(startIndex: Int, endIndex: Int): Int {
        var openIndex = startIndex - 1
        var closeIndex = startIndex - 1
        while (true) {
            openIndex = findNextOpenBrace(openIndex + 1, endIndex)
            closeIndex = findNextCloseBrace(closeIndex + 1, endIndex)
            if (closeIndex >= endIndex)
                return endIndex
            if (openIndex >= endIndex)
                return closeIndex
            if (openIndex > closeIndex)
                return closeIndex
        }
    }

    private fun findNextCorrespondingComma(startIndex: Int, endIndex: Int): Int {
        return findNextCorrespondingChar(startIndex, endIndex, ',')
    }

    /** finds the next 'char' not quoted and not inside a "{ ... }" */
    private fun findNextCorrespondingChar(startIndex: Int, endIndex: Int, char: Char): Int {
        var index = startIndex
        while (true) {
            val commaIndex = findNext(index, endIndex, char)
            if (commaIndex >= endIndex)
                return endIndex
            val openIndex = findNextOpenBrace(index, endIndex)
            if (openIndex >= endIndex || commaIndex < openIndex)
                return commaIndex
            val closeIndex = findCorrespondingCloseBrace(openIndex, endIndex)
            if (commaIndex > closeIndex)
                return commaIndex
            index = closeIndex
        }
    }


    /**
     * finds the index of the next character matching the given character not quoted
     *
     * @param char the char to search. null for whitespaces
     */
    private fun findNext(startIndex: Int, endIndex: Int, char: Char): Int {
        return findNext(startIndex, endIndex) { it == char }
    }

    private fun findNext(startIndex: Int, endIndex: Int, predicate: (Char) -> Boolean): Int {
        var inQuotes = false
        var oneQuote = false
        for (index in startIndex until endIndex) {
            val c = message[index]
            var cRead: Char? = null
            when {
                oneQuote -> {
                    if (c != '\'')
                        inQuotes = !inQuotes
                    if (!inQuotes)
                        cRead = c
                    oneQuote = false
                }

                inQuotes -> {
                    if (c == '\'')
                        oneQuote = true
                }

                c == '\'' -> {
                    oneQuote = true
                }

                else -> {
                    cRead = c
                }
            }
            if (cRead != null && predicate(cRead))
                return index
        }
        return endIndex
    }

    /**
     * Returns the new start index, trimmed by whitespace.
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun trimStart(startIndex: Int, endIndex: Int): Int {
        var index = startIndex
        while (index < endIndex && message[index].isWhitespace())
            index++
        return index
    }

    /**
     * Returns the new end index, trimmed by whitespace.
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun trimEnd(startIndex: Int, endIndex: Int): Int {
        var index = endIndex - 1
        while (index >= startIndex && message[index].isWhitespace())
            index--
        return index + 1
    }

    /**
     * Reads the text in the given range and removes quotes.
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun readText(startIndex: Int, endIndex: Int): CharSequence {
        // no quotation?
        val subSequence = message.subSequence(startIndex, endIndex)
        if (subSequence.indexOf('\'') < 0)
            return subSequence

        // build the text and remove quotation
        val buffer = StringBuilder(endIndex - startIndex)
        var inQuotes = false
        var oneQuote = false
        for (index in startIndex until endIndex) {
            val c = message[index]
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

                c == '\'' -> {
                    oneQuote = true
                }

                else -> {
                    buffer.append(c)
                }
            }
        }
        return buffer.toString()
    }
}