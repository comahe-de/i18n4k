package de.comahe.i18n4k.messages.formatter.parsing

/** Style part that is just an argument and not a nested message part */
data class StylePartArgument(val value: CharSequence) : StylePart {
    override val maxParameterIndex: Int
        get() = -1
    override val hasNamedParameters: Boolean
        get() = false

}
