package de.comahe.i18n4k.messages.formatter.parsing

data class StylePartSimple(val data: MessagePart) : StylePart {

    override val maxParameterIndex: Int
        get() = data.maxParameterIndex

}
