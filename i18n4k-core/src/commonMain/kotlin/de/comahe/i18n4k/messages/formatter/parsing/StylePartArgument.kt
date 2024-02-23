package de.comahe.i18n4k.messages.formatter.parsing

/** Style part that is just an argument and not a nested message part */
data class StylePartArgument(val value: CharSequence) : StylePart {
    override fun fillInParameterNames(names: MutableSet<CharSequence>) {
    }
}
