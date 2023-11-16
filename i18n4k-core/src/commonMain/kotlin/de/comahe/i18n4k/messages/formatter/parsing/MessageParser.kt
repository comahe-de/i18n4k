package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.i18n4k
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

/**
 * Parser for the
 * [de.comahe.i18n4k.messages.formatter.MessageFormatterDefault] format
 */
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
        if (index >= endIndex)
            return MessagePartText(readText(startIndex, endIndex))

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

            try {
                parts += parseParameterPattern(lastIndex, index)
            } catch (e: MessageParseException) {
                if (!ignoreMessageParseErrors)
                    throw e
            }

            lastIndex = index + 1
            if (lastIndex >= endIndex)
                break

            index = findNextOpenBrace(index, endIndex)
        }
        if (parts.size == 1)
            return parts[0]
        return MessagePartList(parts.toImmutableList())
    }

    /**
     * Parses the message in the given range, but trims whitespaces at the
     * beginning and end.
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
    private fun parseParameterPattern(startIndex: Int, endIndex: Int): MessagePartParam {
        var lastIndex = startIndex

        // read parameter index
        var index = findNextCorrespondingComma(lastIndex, endIndex)
        val parameterIndex = parseParameterIndex(lastIndex, index)
        if (index >= endIndex)
            return MessagePartParam(parameterIndex, null, null)

        // read type
        lastIndex = index + 1
        index = findNextCorrespondingComma(lastIndex, endIndex)
        val parameterType = readText(lastIndex, index).trim()
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
     * Parses the index of the parameter
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseParameterIndex(startIndex: Int, endIndex: Int): Int? {
        val text = readText(startIndex, endIndex).trim()
        if (text == "~")
            return null
        val index = text.toString().toIntOrNull()
            ?: throw MessageParseException("Parameter index (\"$text\") is not a valid integer number! In Message: $message")
        if (index < 0)
            throw MessageParseException("Parameter index (\"$text\") must be positive or zero! In Message: $message")
        return index
    }


    /**
     * Parses the style of the parameter, e.g. "a: b | c: d | e"
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseStyle(startIndex: Int, endIndex: Int): StylePart {
        var index = findVerticalBar(startIndex, endIndex)
        if (index >= endIndex)
            return parseStylePart(startIndex, endIndex)

        var lastIndex = startIndex
        val parts = mutableListOf<StylePart>()
        parts += parseStylePart(lastIndex, index)

        while (true) {
            lastIndex = index + 1
            index = findVerticalBar(lastIndex, endIndex)
            parts += parseStylePart(lastIndex, index)
            if (index >= endIndex)
                break
        }

        return StylePartList(parts.toImmutableList())
    }

    /**
     * Parses the style part, e.g. "a: b"
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseStylePart(startIndex: Int, endIndex: Int): StylePart {
        val index = findColon(startIndex, endIndex)
        if (index < endIndex) {
            return StylePartNamed(
                names = parseNames(startIndex, index),
                data = parseMessagePartTrimmed(index + 1, endIndex),
            )
        }
        return StylePartSimple(parseMessagePartTrimmed(startIndex, endIndex))
    }

    /**
     * Parses the style names, e.g. "a / b / c "
     *
     * @param startIndex - the start index, inclusive.
     * @param endIndex - the end index, exclusive.
     */
    private fun parseNames(startIndex: Int, endIndex: Int): ImmutableSet<CharSequence> {
        var lastIndex = startIndex
        val names = mutableSetOf<CharSequence>()
        while (true) {
            val index = findSlash(lastIndex, endIndex)
            names += readText(lastIndex, index).trim()
            if (index >= endIndex)
                break
            lastIndex = index + 1
        }
        return names.toImmutableSet()
    }

    /** finds the next "{" not quoted */
    private fun findNextOpenBrace(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, '{')
    }

    /** finds the next "}" not quoted */
    private fun findNextCloseBrace(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, '}')

    }

    /** finds the next "," not quoted */
    private fun findNextComma(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, ',')

    }

    /** finds the next "|" not quoted */
    private fun findVerticalBar(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, '|')

    }

    /** finds the next ":" not quoted */
    private fun findColon(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, ':')

    }

    /** finds the next "/" not quoted */
    private fun findSlash(startIndex: Int, endIndex: Int): Int {
        return findNext(startIndex, endIndex, '/')

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

    /** finds the next "," not quoted and not inside a "{ ... }" */
    private fun findNextCorrespondingComma(startIndex: Int, endIndex: Int): Int {
        var index = startIndex
        while (true) {
            val commaIndex = findNextComma(index, endIndex)
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
     * finds the index of the next character matching the given character not
     * quoted
     */
    private fun findNext(startIndex: Int, endIndex: Int, char: Char): Int {
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
            if (cRead != null && cRead == char)
                return index

        }
        return endIndex
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