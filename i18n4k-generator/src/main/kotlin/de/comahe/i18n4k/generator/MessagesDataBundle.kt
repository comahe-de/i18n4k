package de.comahe.i18n4k.generator

import de.comahe.i18n4k.messages.formatter.MessageFormatter
import java.util.Locale

/**
 * A message bundle with several translations.
 */
class MessagesDataBundle(
    /** name  */
    val name: BundleName,
    /** The used [MessageFormatter] to handle parameters in the message strings */
    private val messageFormatter: MessageFormatter
) {
    /** maps locale IDs to translations */
    val messageDataMap: MutableMap<Locale, MessagesData> = mutableMapOf()

    /** ass a translations */
    fun addMessagesData(messagesData: MessagesData) {
        messageDataMap[messagesData.locale] = messagesData
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(name).append(" >> ")
        messageDataMap.values.forEach { data -> sb.append(data.locale).append(", ") }
        return sb.toString()
    }

    /**
     * finds the names of used parameter of a message string (see [MessageFormatter]).
     *
     * Empty if there are no parameters.
     */
    fun getMessageParametersNames(key: String): Set<CharSequence> =
        messageDataMap.values
            .map { it.messages[key] }
            .map { message ->
                if (message == null) setOf()
                else messageDataMap.keys
                    .map { locale ->
                        messageFormatter.getMessageParametersNames(message, locale)
                    }
            }.flatten()
            .fold(mutableSetOf<CharSequence>()) { result, current ->
                result.addAll(current); result
            }
}