package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale

/** Format special types of values
 *
 * Used by [MessageFormatterDefault]
 */
interface MessageValueFormatter {

    /**
     * Formats the given [value] in the given [formatType]
     *
     * @return
     *      the formatted string, null is the value object had an incorrect type or value.
     * */
    fun format(
        value: Any,
        formatType: CharSequence,
        formatStyle: CharSequence?,
        locale: Locale
    ): CharSequence?
}