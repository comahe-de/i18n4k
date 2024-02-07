package de.comahe.i18n4k.messages.formatter.parsing


/** Style part that is nested message part */
data class StylePartMessage(val messagePart: MessagePart) : StylePart {

    override val maxParameterIndex: Int
        get() = messagePart.maxParameterIndex
    override val hasNamedParameters: Boolean
        get() = messagePart.hasNamedParameters

}
