package de.comahe.i18n4k.messages.providers

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.language

/**
 * Loads a messages array from a text file
 *
 * Format
 * * Elements are split by a line starting with "^" followed by optional whitespace and a new line
 * * All other "^" at beginnings of line are removed
 * * If a line in a text starts with "^" and additional "^" has to be added before.
 * * The first element is the locale tag, see [de.comahe.i18n4k.toTag].
 * * The following elements form the array of message strings.
 *
 * Example
 *
 * ```
 * en_US
 * ^
 * yes
 * ^
 * no
 * ^
 * Multi
 * Line
 * Text
 * with empty
 *
 * lines
 * ^
 * ^^Text with a "^" at the beginning
 * ^
 * Mult line
 * ^^text with a "^" at
 * the beginning
 * ^
 * some text
 * ^
 * ^
 * above is a null text
 * ^
 *
 * ^
 * above is a empty text
 *
 *
 * ```
 *
 */
@Suppress("unused")
abstract class MessagesProviderViaLoadingText(
    /** Expected locale. Null for not checking. */
    private val expectedLocale: Locale? = null
) : MessagesProviderViaLoading() {

    /** Load the text. Will only be called once! */
    protected abstract fun loadText(): String

    override fun load(): Pair<Locale, List<String?>> {
        val tempList = mutableListOf<String?>()
        val current = StringBuilder()
        var isNull = true
        for (line in loadText().lineSequence()) {
            if (line.startsWith("^") && line.trim() == "^") {// separator!
                tempList.add(if (isNull) null else current.toString())
                current.setLength(0)
                isNull = true
                continue
            }
            if (!isNull)
                current.append("\n")
            current.append(
                if (line.startsWith("^"))
                    line.substring(1)
                else
                    line
            )
            isNull = false
        }
        tempList.add(current.toString())

        // first line is the locale code, the rest are the strings.
        val locale = (if (tempList.isEmpty()) null else tempList[0])?.let { forLocaleTag(it) }
            ?: throw IllegalArgumentException("Locale tag of loaded data is null")

        if (expectedLocale != null && locale != expectedLocale) {
            throw IllegalArgumentException("Language code of loaded data (${tempList[0]}) does not match expected code (${expectedLocale}) ")
        }
        return Pair(locale, tempList.subList(1, tempList.size))
    }
}
