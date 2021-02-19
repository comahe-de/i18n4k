package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale

/** The formatter used to insert and format the parameters of the message text */
interface MessageFormatter {
    /** Formats and returns the message
     * @param message
     *      the original message string (with parameters)
     * @param parameters
     *      List of parameters
     * @param locale
     *           locale to be used
     * @return
     *
     * */
    fun format(message: String, parameters: List<Any>, locale: Locale): String


    /** returns the max used parameter index in the string. "-1" if there is no parameter */
    fun getMaxParameterIndex(message: String, locale: Locale): Int {
        var parameterIndex = -1
        val l = object : AbstractList<Any>() {
            override val size: Int
                get() = Int.MAX_VALUE

            override fun get(index: Int): Any {
                if (index > parameterIndex)
                    parameterIndex = index
                return ""
            }
        }
        format(message, l, locale)
        return parameterIndex
    }
}