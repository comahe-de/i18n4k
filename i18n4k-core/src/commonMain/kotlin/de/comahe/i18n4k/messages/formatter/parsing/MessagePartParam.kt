package de.comahe.i18n4k.messages.formatter.parsing

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.strings.LocalizedString
import kotlin.math.max

data class MessagePartParam(val index: Int?, val type: CharSequence?, var style: StylePart?) :
    MessagePart {

    override val maxParameterIndex: Int
        get() = max(index ?: -1, style?.maxParameterIndex ?: -1)


    override fun format(
        result: StringBuilder,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ) {
        var value: Any? =
            if (index == null)
                null
            else {
                if (index < 0 || index >= parameters.size) {
                    result.append("{").append(index).append("}")
                    return
                }
                parameters[index]
            }

        if (type == null) {
            if (value == null)
                result.append("{~}")
            else
                result.append(value)
            return
        }
        val formatter = context.getFormatterFor(type)
        if (formatter == null) {
            if (value == null)
                result.append("{~}")
            else
                result.append(value)
            return
        }

        formatter.format(result, value, type, style, parameters, locale, context)
    }

}
