package de.comahe.i18n4k.messages.formatter.parsing


/** Style part that is nested message part */
data class StylePartMessage(val messagePart: MessagePart) : StylePart {

    override fun fillInParameterNames(names: MutableList<Pair<CharSequence, CharSequence?>>) {
        messagePart.fillInParameterNames(names)
    }
}
