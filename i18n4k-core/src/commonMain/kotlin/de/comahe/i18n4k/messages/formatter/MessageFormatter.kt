package de.comahe.i18n4k.messages.formatter

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



    /** returns the max used parameter index in the string. "-1" if there is no parameter */
    fun getMessageParametersNames(message: String, locale: Locale): Set<CharSequence> {
        val parameterNames = mutableSetOf<CharSequence>()
        val parameters = object : MessageParameters {
            override fun get(name: CharSequence): Any? {
                parameterNames += name
                return null
            }
        }
        format(message, parameters, locale)
        return parameterNames
    }
}