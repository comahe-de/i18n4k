package de.comahe.i18n4k.messages.formatter.parsing


/** Style part that is nested message part */
data class StylePartMessage(val messagePart: MessagePart) : StylePart {

    override fun fillInParameterNames(names: MutableSet<CharSequence>) {
        messagePart.fillInParameterNames(names)
    }
}
