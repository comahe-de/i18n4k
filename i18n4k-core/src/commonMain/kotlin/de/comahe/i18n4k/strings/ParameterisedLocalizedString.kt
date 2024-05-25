package de.comahe.i18n4k.strings

import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.NameToIndexMapper
import de.comahe.i18n4k.messages.NameToIndexMapperNumbersFrom0
import de.comahe.i18n4k.messages.formatter.MessageParameters
import de.comahe.i18n4k.messages.formatter.MessageParametersList

/**
 * [LocalizedString] that can have parameters.
 *
 * The parameters must be specified in the [message] according to the currently set
 * [MessageFormatter] in [i18n4k] ([I18n4kConfig.messageFormatter])
 */
@Suppress("MemberVisibilityCanBePrivate")
class ParameterisedLocalizedString(
    /** Message which [toString]-method must evaluate to a string to be used in [MessageFormatter.format]
     * (set in [i18n4k] ([I18n4kConfig.messageFormatter]))
     * */
    private val message: Any,
    /** set of parameters */
    private val parameter: MessageParameters,
) :
    AbstractLocalizedString() {

    constructor(
        message: Any, parameter: List<Any>,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ) : this(
        message,
        MessageParametersList(parameter, nameMapper)
    )

    override fun toString(locale: Locale?): String {
        val l = locale ?: i18n4k.locale
        return i18n4k.messageFormatter.format(
            (message as? LocalizedString)?.toString(l) ?: message.toString(),
            parameter, l
        )
    }
}