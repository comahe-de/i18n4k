package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageParameters
import de.comahe.i18n4k.strings.toString

data class MessagePartParam(
    val name: CharSequence,
    val type: CharSequence? = null,
    var style: StylePart? = null
) :
    MessagePart {

    override fun fillInParameterNames(names: MutableSet<CharSequence>) {
       names += name
        style?.fillInParameterNames(names)
    }

    override fun format(
        result: StringBuilder,
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    ) {
        val value: Any? = parameters[name]

        if (type == null) {
            if (value == null)
                result.append('{').append(name).append('}')
            else
                result.append(value.toString(locale))
            return
        }
        val formatter = context.getFormatterFor(type)
        if (formatter == null) {
            if (value == null)
                result.append('{').append(name).append('}')
            else
                result.append(value.toString(locale))
            return
        }

        formatter.format(result, value, type, style, parameters, locale, context)
    }

}
