package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.NameToIndexMapperNumbersFrom0

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
    fun format(message: String, parameters: MessageParameters, locale: Locale): String

    /**
     * Formats and returns the message
     *
     * @param message the original message string (with parameters)
     * @param parameters List of parameters
     * @param locale locale to be used
     * @return
     */
    fun format(message: String, parameters: List<Any>, locale: Locale): String =
        format(message, MessageParametersList(parameters, NameToIndexMapperNumbersFrom0), locale)


    /**
     * Returns the all used parameter names ([Pair.first]) with they optional value types
     * ([Pair.second]) in the string.
     *
     * Empty if there is no parameter.
     */
    fun getMessageParametersNames(message: String, locale: Locale): List<Pair<CharSequence, CharSequence?>> {
        val parameterNames = mutableListOf<Pair<CharSequence, CharSequence?>>()
        val parameters = object : MessageParameters {
            override fun get(name: CharSequence): Any? {
                parameterNames.add(name to null)
                return null
            }
        }
        format(message, parameters, locale)
        return parameterNames
    }
}