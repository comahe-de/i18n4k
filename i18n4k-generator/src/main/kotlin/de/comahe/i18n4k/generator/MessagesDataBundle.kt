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
    fun getMessageParametersNames(key: String): Map<String, String?> =
        messageDataMap.values
            .map { it.messages[key] }
            .map { message ->
                if (message == null) setOf()
                else messageDataMap.keys
                    .map { locale ->
                        messageFormatter.getMessageParametersNames(message, locale)
                    }
            }
            .flatten()
            .flatten()
            .fold(mutableMapOf<String, String?>()) { result, current ->
                val split = current.split(Regex(":\\s*"))
                val name = split[0]
                val type = split.getOrNull(1)
                if (name in result) {
                    if (type != null && result[name] == null)
                        result[name] = type
                } else result[name] = type
                result
            }
}