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

    /** find the parameter count of a message string (see [MessageFormatter]) */
    fun getMaxParameterIndexForKey(key: String): Int =
        messageDataMap.values
            .map { it.messages[key] }
            .map { message ->
                if (message == null) 0
                else messageDataMap.keys
                    .map { locale ->
                        messageFormatter.getMaxParameterIndex(message, locale)
                    }
                    .max() ?: 0
            }
            .max() ?: -1
}